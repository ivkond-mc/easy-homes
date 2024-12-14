package ivkond.mc.mods.eh.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import ivkond.mc.mods.eh.storage.HomeRepository;
import ivkond.mc.mods.eh.utils.HomeNameSuggestionProvider;
import ivkond.mc.mods.eh.utils.HomeUtils;
import ivkond.mc.mods.eh.utils.I18N;
import ivkond.mc.mods.eh.utils.Log;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class RenHomeCommand {
    private static final HomeRepository homes = HomeRepository.INSTANCE;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> home = literal("renhome")
                .then(argument("old_name", StringArgumentType.string())
                        .suggests(HomeNameSuggestionProvider.INSTANCE)
                        .then(argument("new_name", StringArgumentType.greedyString())
                                .suggests(HomeNameSuggestionProvider.INSTANCE)
                                .executes(RenHomeCommand::renameHome)));

        dispatcher.register(home);
    }

    private static int renameHome(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer player = source.getPlayerOrException();
        String playerId = player.getStringUUID();

        String oldName = context.getArgument("old_name", String.class);
        if (HomeUtils.isInvalidName(oldName)) {
            player.sendSystemMessage(I18N.errorInvalidHomeName(oldName));
            return 0;
        }

        String newName = context.getArgument("new_name", String.class);
        if (HomeUtils.isInvalidName(newName)) {
            player.sendSystemMessage(I18N.errorInvalidHomeName(newName));
            return 0;
        }

        Log.info("Rename players {} home {} to {}", player.getDisplayName().getString(), oldName, newName);

        boolean oldHomeExists = homes.exists(playerId, oldName);
        if (!oldHomeExists) {
            player.displayClientMessage(I18N.errorHomeNotFound(oldName), true);
            return 0;
        }

        homes.renameHome(playerId, oldName, newName);

        player.displayClientMessage(I18N.commandRenHomeSuccess(oldName, newName), true);

        return Command.SINGLE_SUCCESS;
    }
}
