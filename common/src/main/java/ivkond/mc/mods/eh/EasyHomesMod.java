package ivkond.mc.mods.eh;

import com.mojang.brigadier.CommandDispatcher;
import eu.midnightdust.lib.config.MidnightConfig;
import ivkond.mc.mods.eh.commands.*;
import ivkond.mc.mods.eh.config.EasyHomesConfig;
import ivkond.mc.mods.eh.integration.xaero.XaerosMinimapIntegration;
import ivkond.mc.mods.eh.storage.HomeRepository;
import ivkond.mc.mods.eh.utils.Log;
import ivkond.mc.mods.eh.utils.PathUtils;
import ivkond.mc.mods.eh.utils.Platform;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.nio.file.Path;

public final class EasyHomesMod {
    public static final String MOD_ID = "easy_homes";

    private static final HomeRepository homes = HomeRepository.INSTANCE;

    public static void init(Platform platformHelper) {
        Log.info("Initializing Easy Homes Mod");

        MidnightConfig.init(MOD_ID, EasyHomesConfig.class);

        XaerosMinimapIntegration.init(platformHelper);
    }

    public static void onServerStarted(MinecraftServer server) {
        Path dataDir = PathUtils.getOrCreateDataDir(server);
        Log.info("Initializing Easy Homes storage");
        homes.init(dataDir);
    }

    public static void onServerStopping() {
        homes.forgetAll();
    }

    public static void onPlayerLoggedIn(ServerPlayer player) {
        Log.debug("Player {} logged in. Now load configuration", player.getDisplayName().getString());
        homes.loadConfig(player.getStringUUID());
    }

    public static void onPlayerLoggedOut(ServerPlayer player) {
        Log.debug("Player {} logged out. Now persist and clear configuration", player.getDisplayName().getString());
        homes.forgetConfig(player.getStringUUID());
    }

    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        HomeCommand.register(dispatcher);
        HomesCommand.register(dispatcher);
        SetHomeCommand.register(dispatcher);
        RenHomeCommand.register(dispatcher);
        DelHomeCommand.register(dispatcher);
        BackCommand.register(dispatcher);
    }
}
