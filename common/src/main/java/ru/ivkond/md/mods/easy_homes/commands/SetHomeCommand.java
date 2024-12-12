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

public class SetHomeCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> home = literal("sethome")
                .executes(SetHomeCommand::setDefaultHome)
                .then(argument("name", StringArgumentType.word())
                        .executes(SetHomeCommand::setHome));

        dispatcher.register(home);
    }

    private static int setHome(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String home = context.getArgument("name", String.class);
        return doSetHome(context, home);
    }

    private static int setDefaultHome(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        return doSetHome(context, SimpleHomesConfig.DEFAULT_HOME);
    }

    private static int doSetHome(CommandContext<CommandSourceStack> context, String home) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer serverPlayer = source.getPlayerOrException();

        // TODO: Error handling
        CommandHandler.setHome(serverPlayer, home);
        serverPlayer.sendSystemMessage(I18N.commandSetHomeSuccess());

        return Command.SINGLE_SUCCESS;
    }
}
