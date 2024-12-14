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
import ivkond.mc.mods.eh.utils.I18N;
import ivkond.mc.mods.eh.utils.Log;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;

import java.time.Duration;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

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
        String playerId = player.getStringUUID();
        ServerLevel currentLevel = source.getLevel();

        Log.info("Teleporting {} to home {}", player.getName().getString(), homeName);

        HomeLocation home = homes.findHome(playerId, homeName);
        if (home == null) {
            player.displayClientMessage(I18N.errorHomeNotFound(homeName), true);
            return 0;
        }

        Duration lockDuration = homes.getLockExpiration(playerId);
        if (lockDuration.isNegative()) {
            player.displayClientMessage(I18N.commandHomeLocked(lockDuration), true);
            return 0;
        }

        ResourceLocation levelLocation = ResourceLocation.parse(home.dimension());
        ResourceKey<Level> levelKey = ResourceKey.create(Registries.DIMENSION, levelLocation);
        ServerLevel targetLevel = source.getServer().getLevel(levelKey);
        if (targetLevel == null) {
            player.displayClientMessage(I18N.errorUnknownLevel(home.dimension()), true);
            return 0;
        }

        // TeleportCommand#performTeleport
        BlockPos blockPos = BlockPos.containing(home.x(), home.y(), home.z());
        if (!ServerLevel.isInSpawnableBounds(blockPos)) {
            player.displayClientMessage(I18N.errorInvalidPosition(), true);
            return 0;
        }

        playDecorations(currentLevel, player.blockPosition(), ParticleTypes.PORTAL);
        player.teleportTo(targetLevel, home.x(), home.y(), home.z(), home.rotY(), home.rotX());
        playDecorations(targetLevel, blockPos, ParticleTypes.REVERSE_PORTAL);

        homes.updateLockDuration(playerId);

        player.displayClientMessage(I18N.commandHomeSuccess(homeName), true);

        return Command.SINGLE_SUCCESS;
    }

    private static void playDecorations(ServerLevel level, BlockPos pos, SimpleParticleType particles) {
        level.sendParticles(particles, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 20, 0.3d, 0.3d, 0.3d, 0.5d);
        level.playSound(null, pos, SoundEvents.PLAYER_TELEPORT, SoundSource.PLAYERS, 1f, 1f);
    }
}
