package ivkond.mc.mods.eh.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import ivkond.mc.mods.eh.config.EasyHomesConfig;
import ivkond.mc.mods.eh.domain.HomeLocation;
import ivkond.mc.mods.eh.storage.HomeRepository;
import ivkond.mc.mods.eh.utils.HomeNameSuggestionProvider;
import ivkond.mc.mods.eh.utils.HomeUtils;
import ivkond.mc.mods.eh.utils.I18N;
import ivkond.mc.mods.eh.utils.Log;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;

import java.time.Duration;
import java.util.Set;

import static net.minecraft.commands.Commands.*;

public class HomeCommand {
    private static final HomeRepository homes = HomeRepository.INSTANCE;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> home = literal("home")
                .executes(HomeCommand::teleportToDefaultHome)
                .then(argument("name", StringArgumentType.greedyString())
                        .suggests(HomeNameSuggestionProvider.INSTANCE)
                        .executes(HomeCommand::teleportToHome));

        dispatcher.register(home);
    }

    private static int teleportToDefaultHome(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        return doTeleport(context, EasyHomesConfig.DEFAULT_HOME);
    }

    private static int teleportToHome(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String homeName = context.getArgument("name", String.class);
        return doTeleport(context, homeName);
    }

    private static int doTeleport(CommandContext<CommandSourceStack> context, String homeName) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer player = source.getPlayerOrException();

        Log.debug("Teleporting {} to home {}", player.getName().getString(), homeName);

        if (HomeUtils.isInvalidName(homeName)) {
            player.sendOverlayMessage(I18N.errorInvalidHomeName(homeName));
            return 0;
        }

        boolean success = doTeleportInternal(source, homeName);
        return success ? Command.SINGLE_SUCCESS : 0;
    }

    static boolean doTeleportInternal(CommandSourceStack stack, String homeName) throws CommandSyntaxException {
        ServerPlayer player = stack.getPlayerOrException();
        String playerId = player.getStringUUID();
        ServerLevel currentLevel = stack.getLevel();

        if (player.isPassenger() && player.canControlVehicle()) {
            player.sendOverlayMessage(I18N.errorPlayerMounted());
            return false;
        }

        HomeLocation home = homes.findHome(playerId, homeName);
        if (home == null) {
            player.sendOverlayMessage(I18N.errorHomeNotFound(homeName));
            return false;
        }

        if (!player.isCreative()) {
            Duration cooldown = homes.getCooldown(playerId);
            if (cooldown.isPositive()) {
                player.sendOverlayMessage(I18N.commandHomeLocked(cooldown));
                return false;
            }
        }

        Identifier levelLocation = Identifier.parse(home.dimension());
        ResourceKey<Level> levelKey = ResourceKey.create(Registries.DIMENSION, levelLocation);
        ServerLevel targetLevel = stack.getServer().getLevel(levelKey);
        if (targetLevel == null) {
            player.sendOverlayMessage(I18N.errorUnknownLevel(home.dimension()));
            return false;
        }

        // TeleportCommand#performTeleport
        BlockPos blockPos = BlockPos.containing(home.x(), home.y(), home.z());
        if (!ServerLevel.isInSpawnableBounds(blockPos)) {
            player.sendOverlayMessage(I18N.errorInvalidPosition());
            return false;
        }

        playDecorations(currentLevel, player.blockPosition(), ParticleTypes.PORTAL);
        player.teleportTo(targetLevel, home.x(), home.y(), home.z(), Set.of(), home.rotY(), home.rotX(), false);
        playDecorations(targetLevel, blockPos, ParticleTypes.REVERSE_PORTAL);

        homes.onTeleported(playerId, homeName);

        player.sendOverlayMessage(I18N.commandHomeSuccess(homeName));
        return true;
    }

    private static void playDecorations(ServerLevel level, BlockPos pos, SimpleParticleType particles) {
        level.sendParticles(particles, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 20, 0.3d, 0.3d, 0.3d, 0.5d);
        level.playSound(null, pos, SoundEvents.PLAYER_TELEPORT, SoundSource.PLAYERS, 1f, 1f);
    }
}
