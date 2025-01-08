package ivkond.mc.mods.eh.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import ivkond.mc.mods.eh.storage.HomeRepository;
import ivkond.mc.mods.eh.utils.I18N;
import ivkond.mc.mods.eh.utils.Log;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

import static net.minecraft.commands.Commands.literal;

public class BackCommand {
    private static final HomeRepository homes = HomeRepository.INSTANCE;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> home = literal("back")
                .executes(BackCommand::back);

        dispatcher.register(home);
    }

    private static int back(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer player = source.getPlayerOrException();

        Log.info("Teleport player to last visited home {}", player.getDisplayName().getString());

        String lastVisitedHome = homes.getLastVisitedHome(player.getStringUUID());
        if (lastVisitedHome == null) {
            player.displayClientMessage(I18N.errorNoLastVisitedHome(), true);
            return 0;
        }

        HomeCommand.doTeleportInternal(source, lastVisitedHome);

        return Command.SINGLE_SUCCESS;
    }
}
