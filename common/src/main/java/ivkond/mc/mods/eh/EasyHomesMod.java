package ivkond.mc.mods.eh;

import com.mojang.brigadier.CommandDispatcher;
import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import ivkond.mc.mods.eh.client.HomeKeyPressedHandler;
import ivkond.mc.mods.eh.commands.DelHomeCommand;
import ivkond.mc.mods.eh.commands.HomeCommand;
import ivkond.mc.mods.eh.commands.HomesCommand;
import ivkond.mc.mods.eh.commands.SetHomeCommand;
import ivkond.mc.mods.eh.config.EasyHomesConfig;
import ivkond.mc.mods.eh.storage.HomeRepository;
import ivkond.mc.mods.eh.utils.Log;
import ivkond.mc.mods.eh.utils.PathUtils;

import java.nio.file.Path;

public final class EasyHomesMod {
    public static final String MOD_ID = "easy_homes";

    private static final HomeRepository homes = HomeRepository.INSTANCE;

    public static void init() {
        Log.info("Initializing Easy Homes Mod");

        MidnightConfig.init(MOD_ID, EasyHomesConfig.class);
    }

    public static void onServerStared(MinecraftServer server) {
        Path dataDir = PathUtils.getOrCreateDataDir(server);
        Log.info("Initializing Easy Homes storage");
        homes.init(dataDir);
    }

    public static void onServerStopping() {
        homes.unload();
    }

    public static void onClientTick(Minecraft minecraft) {
        HomeKeyPressedHandler.handle(minecraft);
    }

    public static void onPlayerLoggedIn(ServerPlayer player) {
        Log.debug("Player {} logged in. Now load configuration", player.getDisplayName().getString());
        homes.loadPlayerConfig(player.getStringUUID());
    }

    public static void onPlayerLoggedOut(ServerPlayer player) {
        Log.debug("Player {} logged out. Now persist and clear configuration", player.getDisplayName().getString());
        homes.unloadPlayerConfig(player.getStringUUID());
    }

    public static Screen createConfigurationScreen(Screen parent) {
        return MidnightConfig.getScreen(parent, MOD_ID);
    }

    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        HomeCommand.register(dispatcher);
        HomesCommand.register(dispatcher);
        SetHomeCommand.register(dispatcher);
        DelHomeCommand.register(dispatcher);
    }
}
