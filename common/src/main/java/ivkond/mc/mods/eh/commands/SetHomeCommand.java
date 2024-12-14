package ivkond.mc.mods.eh.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import ivkond.mc.mods.eh.config.EasyHomesConfig;
import ivkond.mc.mods.eh.domain.HomeLocation;
import ivkond.mc.mods.eh.storage.HomeRepository;
import ivkond.mc.mods.eh.utils.HomeUtils;
import ivkond.mc.mods.eh.utils.I18N;
import ivkond.mc.mods.eh.utils.Log;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class SetHomeCommand {
    private static final HomeRepository homes = HomeRepository.INSTANCE;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> home = literal("sethome")
                .executes(SetHomeCommand::setDefaultHome)
                .then(argument("name", StringArgumentType.greedyString())
                        .executes(SetHomeCommand::setHome));

        dispatcher.register(home);
    }

    private static int setHome(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String home = context.getArgument("name", String.class);
        return doSetHome(context, home);
    }

    private static int setDefaultHome(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        return doSetHome(context, EasyHomesConfig.DEFAULT_HOME);
    }

    private static int doSetHome(CommandContext<CommandSourceStack> context, String homeName) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer player = source.getPlayerOrException();
        String playerId = player.getStringUUID();

        if ("-".equals(homeName)) {
            homeName = generateHomeName(player);
        }

        if (HomeUtils.isInvalidName(homeName)) {
            player.sendSystemMessage(I18N.errorInvalidHomeName(homeName));
            return 0;
        }

        Log.info("Save player {} current position as home {}", player.getDisplayName().getString(), homeName);

        boolean existingHome = homes.exists(playerId, homeName);

        if (!existingHome && homes.isMaxHomesReached(playerId)) {
            player.displayClientMessage(I18N.commandSetHomeMaxHomesReached(), true);
            return 0;
        }

        ResourceKey<Level> levelResourceKey = player.level().dimension();
        String level = levelResourceKey.location().toString();
        HomeLocation location = new HomeLocation(level, player.getX(), player.getY(), player.getZ(), player.getXRot(), player.getYRot());

        homes.setHome(player.getStringUUID(), homeName, location);

        player.displayClientMessage(I18N.commandSetHomeSuccess(homeName, existingHome), true);

        return Command.SINGLE_SUCCESS;
    }

    private static String generateHomeName(ServerPlayer player) {
        String dimension = player.level().dimension().location().getPath();
        Vec3 position = player.position();
        return String.format("%s_%.0f_%.0f_%.0f", dimension, position.x, position.y, position.z);
    }
}
