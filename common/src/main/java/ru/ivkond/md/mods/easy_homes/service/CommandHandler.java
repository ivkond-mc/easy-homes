package ru.ivkond.md.mods.easy_homes.service;

import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ivkond.md.mods.easy_homes.domain.PlayerHome;

import java.util.List;

public class CommandHandler {
    private static final Logger log = LogManager.getLogger();

    public static void teleport(ServerPlayer player, String home) {
        log.info("Teleporting {} to home {}", player.getName().getString(), home);
    }

    public static List<PlayerHome> getHomes(ServerPlayer player) {
        log.info("List player {} homes", player.getName().getString());
        return List.of();
    }

    public static void setHome(ServerPlayer player) {
        log.info("Save player {} current position as default home", player.getDisplayName().getString());
    }

    public static void setHome(ServerPlayer player, String home) {
        log.info("Save player {} current position as home {}", player.getDisplayName().getString(), home);
    }

    public static void deleteHome(ServerPlayer player, String home) {
        log.info("Delete players {} home {}", player.getDisplayName().getString(), home);
    }
}
