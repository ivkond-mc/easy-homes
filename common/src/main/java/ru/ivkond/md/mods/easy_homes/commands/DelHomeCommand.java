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
import ru.ivkond.md.mods.easy_homes.storage.HomeRepository;
import ru.ivkond.md.mods.easy_homes.utils.HomeNameSuggestionProvider;
import ru.ivkond.md.mods.easy_homes.utils.I18N;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class DelHomeCommand {
    private static final Logger log = LogManager.getLogger();
    private static final HomeRepository homes = HomeRepository.INSTANCE;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> home = literal("delhome")
                .then(argument("name", StringArgumentType.word())
                        .suggests(HomeNameSuggestionProvider.INSTANCE)
                        .executes(DelHomeCommand::deleteHome));

        dispatcher.register(home);
    }

    private static int deleteHome(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer player = source.getPlayerOrException();
        String playerId = player.getStringUUID();
        String name = context.getArgument("name", String.class);

        log.info("Delete players {} home {}", player.getDisplayName().getString(), name);

        if (!homes.exists(playerId, name)) {
            player.displayClientMessage(I18N.errorHomeNotFound(name), true);
            return 0;
        }

        homes.deleteHome(playerId, name);

        player.displayClientMessage(I18N.commandDelHomeSuccess(name), true);

        return Command.SINGLE_SUCCESS;
    }
}
