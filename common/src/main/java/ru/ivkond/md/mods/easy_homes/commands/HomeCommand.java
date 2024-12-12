package ru.ivkond.md.mods.easy_homes.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import ru.ivkond.md.mods.easy_homes.config.SimpleHomesConfig;
import ru.ivkond.md.mods.easy_homes.service.CommandHandler;
import ru.ivkond.md.mods.easy_homes.utils.I18N;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class HomeCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> home = literal("home")
                .executes(HomeCommand::teleportToDefaultHome)
                .then(argument("name", StringArgumentType.word())
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

    private static int doTeleport(CommandContext<CommandSourceStack> context, String home) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer serverPlayer = source.getPlayerOrException();

        // TODO: Error handling
        CommandHandler.teleport(serverPlayer, home);
        serverPlayer.sendSystemMessage(I18N.commandHomeSuccess());

        return Command.SINGLE_SUCCESS;
    }
}
