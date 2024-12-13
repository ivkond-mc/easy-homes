package ru.ivkond.md.mods.easy_homes.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ivkond.md.mods.easy_homes.config.SimpleHomesConfig;
import ru.ivkond.md.mods.easy_homes.domain.HomeLocation;
import ru.ivkond.md.mods.easy_homes.storage.HomeRepository;
import ru.ivkond.md.mods.easy_homes.utils.HomeNameSuggestionProvider;
import ru.ivkond.md.mods.easy_homes.utils.I18N;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class HomeCommand {
    private static final Logger log = LogManager.getLogger();
    private static final HomeRepository homes = HomeRepository.INSTANCE;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> home = literal("home")
                .executes(HomeCommand::teleportToDefaultHome)
                .then(argument("name", StringArgumentType.word())
                        .suggests(HomeNameSuggestionProvider.INSTANCE)
                        .executes(HomeCommand::teleportToHome));

        dispatcher.register(home);
    }

    private static int teleportToDefaultHome(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        return doTeleport(context, SimpleHomesConfig.DEFAULT_HOME);
    }

    private static int teleportToHome(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String homeName = context.getArgument("name", String.class);
        return doTeleport(context, homeName);
    }

    private static int doTeleport(CommandContext<CommandSourceStack> context, String homeName) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer player = source.getPlayerOrException();
        ServerLevel currentLevel = source.getLevel();

        log.info("Teleporting {} to home {}", player.getName().getString(), homeName);

        HomeLocation home = homes.findHome(player.getStringUUID(), homeName);
        if (home == null) {
            player.displayClientMessage(I18N.errorHomeNotFound(homeName), true);
            return 0;
        }

        // FIXME: Caching?
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

        player.displayClientMessage(I18N.commandHomeSuccess(homeName), true);

        return Command.SINGLE_SUCCESS;
    }

    private static void playDecorations(ServerLevel level, BlockPos pos, SimpleParticleType particles) {
        level.sendParticles(particles, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 20, 0.3d, 0.3d, 0.3d, 0.5d);
        level.playSound(null, pos, SoundEvents.PLAYER_TELEPORT, SoundSource.PLAYERS, 1f, 1f);
    }
}
