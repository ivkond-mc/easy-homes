package ru.ivkond.md.mods.easy_homes.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class HomeCommand {
    private static final Logger log = LogManager.getLogger();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> home = literal("home")
                .executes(HomeCommand::teleportToDefaultHome)
                .then(argument("name", StringArgumentType.word())
                        .executes(HomeCommand::teleportToHome));

        dispatcher.register(home);
    }

    private static int teleportToDefaultHome(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer serverPlayer = source.getPlayerOrException();

        teleportToDefaultHome(serverPlayer);

        return Command.SINGLE_SUCCESS;
    }

    private static int teleportToHome(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer serverPlayer = source.getPlayerOrException();
        String name = context.getArgument("name", String.class);

        teleportToHome(serverPlayer, name);

        return Command.SINGLE_SUCCESS;
    }

    private static void teleportToDefaultHome(ServerPlayer serverPlayer) {
        log.info("Teleporting {} to default home", serverPlayer.getName().getString());
    }

    private static void teleportToHome(ServerPlayer serverPlayer, String name) {
        log.info("Teleporting {} to home {}", serverPlayer.getName().getString(), name);
    }
}
