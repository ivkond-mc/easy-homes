package ru.ivkond.md.mods.easy_homes.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import ru.ivkond.md.mods.easy_homes.domain.PlayerHome;
import ru.ivkond.md.mods.easy_homes.service.CommandHandler;
import ru.ivkond.md.mods.easy_homes.utils.I18N;

import java.util.List;

import static net.minecraft.commands.Commands.literal;

public class HomesCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> home = literal("homes")
                .executes(HomesCommand::listHomes);

        dispatcher.register(home);
    }

    private static int listHomes(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer serverPlayer = source.getPlayerOrException();

        List<PlayerHome> homes = CommandHandler.getHomes(serverPlayer);
        Component response = I18N.commandHomesList(homes);
        serverPlayer.sendSystemMessage(response);

        return Command.SINGLE_SUCCESS;
    }
}
