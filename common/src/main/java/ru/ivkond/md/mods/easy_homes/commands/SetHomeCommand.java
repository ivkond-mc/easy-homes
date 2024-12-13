package ru.ivkond.md.mods.easy_homes.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import ru.ivkond.md.mods.easy_homes.config.SimpleHomesConfig;
import ru.ivkond.md.mods.easy_homes.domain.HomeLocation;
import ru.ivkond.md.mods.easy_homes.storage.HomeRepository;
import ru.ivkond.md.mods.easy_homes.utils.I18N;
import ru.ivkond.md.mods.easy_homes.utils.Log;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class SetHomeCommand {
    private static final HomeRepository homes = HomeRepository.INSTANCE;

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

    private static int doSetHome(CommandContext<CommandSourceStack> context, String homeName) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer player = source.getPlayerOrException();
        String playerId = player.getStringUUID();

        Log.info("Save player {} current position as home {}", player.getDisplayName().getString(), homeName);

        boolean existingHome = homes.exists(playerId, homeName);

        ResourceKey<Level> levelResourceKey = player.level().dimension();
        String level = levelResourceKey.location().toString();
        HomeLocation location = new HomeLocation(level, player.getX(), player.getY(), player.getZ(), player.getXRot(), player.getYRot());

        homes.setHome(player.getStringUUID(), homeName, location);

        player.displayClientMessage(I18N.commandSetHomeSuccess(homeName, existingHome), true);

        return Command.SINGLE_SUCCESS;
    }
}
