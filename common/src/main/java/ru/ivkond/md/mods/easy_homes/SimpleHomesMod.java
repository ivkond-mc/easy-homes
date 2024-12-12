package ru.ivkond.md.mods.easy_homes;

import com.mojang.brigadier.CommandDispatcher;
import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ivkond.md.mods.easy_homes.commands.DelHomeCommand;
import ru.ivkond.md.mods.easy_homes.commands.HomeCommand;
import ru.ivkond.md.mods.easy_homes.commands.HomesCommand;
import ru.ivkond.md.mods.easy_homes.commands.SetHomeCommand;
import ru.ivkond.md.mods.easy_homes.config.SimpleHomesConfig;
import ru.ivkond.md.mods.easy_homes.utils.PathUtils;

import java.nio.file.Path;

public final class SimpleHomesMod {
    public static final String MOD_ID = "easy_homes";

    private static final Logger log = LogManager.getLogger(MOD_ID);

    public static void init() {
        log.info("Initializing Easy Homes Mod");

        MidnightConfig.init(MOD_ID, SimpleHomesConfig.class);
    }

    public static void onServerStared(MinecraftServer server) {
        Path dataDir = PathUtils.getOrCreateDataDir(server);
        System.out.println("data: " + dataDir);
    }

    public static Screen createConfigurationScreen(Screen parent) {
        return MidnightConfig.getScreen(parent, MOD_ID);
    }

    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext) {
        HomeCommand.register(dispatcher);
        HomesCommand.register(dispatcher, buildContext);
        SetHomeCommand.register(dispatcher, buildContext);
        DelHomeCommand.register(dispatcher, buildContext);
    }
}
