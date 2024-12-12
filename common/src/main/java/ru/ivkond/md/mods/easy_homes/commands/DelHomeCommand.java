package ru.ivkond.md.mods.easy_homes.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import ru.ivkond.md.mods.easy_homes.service.CommandHandler;
import ru.ivkond.md.mods.easy_homes.utils.I18N;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class DelHomeCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> home = literal("delhome")
                .then(argument("name", StringArgumentType.word())
                        .executes(DelHomeCommand::deleteHome));

        dispatcher.register(home);
    }

    private static int deleteHome(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer serverPlayer = source.getPlayerOrException();
        String home = context.getArgument("name", String.class);

        // TODO: Error handling
        CommandHandler.deleteHome(serverPlayer, home);
        serverPlayer.sendSystemMessage(I18N.commandDelHomeSuccess());

        return 0;
    }
}
