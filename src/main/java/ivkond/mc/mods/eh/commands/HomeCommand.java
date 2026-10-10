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
import ivkond.mc.mods.eh.utils.OverlayMessages;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
//? if <1.19.3 {
/*import net.minecraft.core.Registry;
*///? } else {
import net.minecraft.core.registries.Registries;
//? }
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
            OverlayMessages.send(player, I18N.errorInvalidHomeName(homeName));
            return 0;
        }

        boolean success = doTeleportInternal(source, homeName);
        return success ? Command.SINGLE_SUCCESS : 0;
    }

    static boolean doTeleportInternal(CommandSourceStack stack, String homeName) throws CommandSyntaxException {
        ServerPlayer player = stack.getPlayerOrException();
        String playerId = player.getStringUUID();
        ServerLevel currentLevel = stack.getLevel();

        //? if <1.19.3 {
        /*// Player.canControlVehicle() only exists since 1.19.3
        if (player.isPassenger()) {
            OverlayMessages.send(player, I18N.errorPlayerMounted());
            return false;
        }*/
        //? } else if <1.21.2 {
        /*if (player.isPassenger() && player.getVehicle().getControllingPassenger() == player) {
            OverlayMessages.send(player, I18N.errorPlayerMounted());
            return false;
        }*/
        //? } else {
        if (player.isPassenger() && player.canControlVehicle()) {
            OverlayMessages.send(player, I18N.errorPlayerMounted());
            return false;
        }
        //? }

        HomeLocation home = homes.findHome(playerId, homeName);
        if (home == null) {
            OverlayMessages.send(player, I18N.errorHomeNotFound(homeName));
            return false;
        }

        if (!player.isCreative()) {
            Duration cooldown = homes.getCooldown(playerId);
            if (cooldown.isPositive()) {
                OverlayMessages.send(player, I18N.commandHomeLocked(cooldown));
                return false;
            }
        }

        Identifier levelLocation = Identifier.parse(home.dimension());
        //? if <1.19.3 {
        /*ResourceKey<Level> levelKey = ResourceKey.create(Registry.DIMENSION_REGISTRY, levelLocation);*/
        //? } else {
        ResourceKey<Level> levelKey = ResourceKey.create(Registries.DIMENSION, levelLocation);
        //? }
        ServerLevel targetLevel = stack.getServer().getLevel(levelKey);
        if (targetLevel == null) {
            OverlayMessages.send(player, I18N.errorUnknownLevel(home.dimension()));
            return false;
        }

        // TeleportCommand#performTeleport
        BlockPos blockPos = BlockPos.containing(home.x(), home.y(), home.z());
        if (!ServerLevel.isInSpawnableBounds(blockPos)) {
            OverlayMessages.send(player, I18N.errorInvalidPosition());
            return false;
        }

        playDecorations(currentLevel, player.blockPosition(), ParticleTypes.PORTAL);
        //? if <1.20.1 {
        /*// no relative-argument Set overload before 1.20.1
        player.teleportTo(targetLevel, home.x(), home.y(), home.z(), home.rotY(), home.rotX());*/
        //? } else if <1.21.2 {
        /*player.teleportTo(targetLevel, home.x(), home.y(), home.z(), Set.of(), home.rotY(), home.rotX());*/
        //? } else {
        player.teleportTo(targetLevel, home.x(), home.y(), home.z(), Set.of(), home.rotY(), home.rotX(), false);
        //? }
        playDecorations(targetLevel, blockPos, ParticleTypes.REVERSE_PORTAL);

        homes.onTeleported(playerId, homeName);

        OverlayMessages.send(player, I18N.commandHomeSuccess(homeName));
        return true;
    }

    private static void playDecorations(ServerLevel level, BlockPos pos, SimpleParticleType particles) {
        level.sendParticles(particles, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 20, 0.3d, 0.3d, 0.3d, 0.5d);
        level.playSound(null, pos, SoundEvents.PLAYER_TELEPORT, SoundSource.PLAYERS, 1f, 1f);
    }
}
