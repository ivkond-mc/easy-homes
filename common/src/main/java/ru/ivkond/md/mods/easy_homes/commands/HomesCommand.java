package ru.ivkond.md.mods.easy_homes.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ivkond.md.mods.easy_homes.domain.PlayerHomes;
import ru.ivkond.md.mods.easy_homes.storage.HomeRepository;
import ru.ivkond.md.mods.easy_homes.utils.I18N;

import static net.minecraft.commands.Commands.literal;

public class HomesCommand {
    private static final Logger log = LogManager.getLogger();
    private static final HomeRepository homes = HomeRepository.INSTANCE;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> home = literal("homes")
                .executes(HomesCommand::listHomes);

        dispatcher.register(home);
    }

    private static int listHomes(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer player = source.getPlayerOrException();

        log.info("List player {} homes", player.getDisplayName().getString());
        PlayerHomes playerHomes = homes.getHomes(player.getStringUUID());

        player.sendSystemMessage(I18N.commandHomesList(playerHomes));

        return Command.SINGLE_SUCCESS;
    }
}
