package ivkond.mc.mods.eh.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import ivkond.mc.mods.eh.domain.PlayerHomes;
import ivkond.mc.mods.eh.storage.HomeRepository;
import ivkond.mc.mods.eh.utils.I18N;
import ivkond.mc.mods.eh.utils.Log;

import static net.minecraft.commands.Commands.literal;

public class HomesCommand {
    private static final HomeRepository homes = HomeRepository.INSTANCE;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> home = literal("homes")
                .executes(HomesCommand::listHomes);

        dispatcher.register(home);
    }

    private static int listHomes(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer player = source.getPlayerOrException();

        Log.info("List player {} homes", player.getDisplayName().getString());
        PlayerHomes playerHomes = homes.getHomes(player.getStringUUID());

        player.sendSystemMessage(I18N.commandHomesList(playerHomes));

        return Command.SINGLE_SUCCESS;
    }
}
