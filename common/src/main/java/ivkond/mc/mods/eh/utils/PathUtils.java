package ivkond.mc.mods.eh.utils;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PathUtils {
    private static final String DIR_DATA = "data";
    private static final String DIR_EASY_HOMES = "easy_homes";

    private PathUtils() {
    }

    public static Path getOrCreateDataDir(MinecraftServer server) {
        Path data = server.getWorldPath(LevelResource.ROOT).toAbsolutePath().normalize().resolve(DIR_DATA);
        Path easyHomes = data.resolve(DIR_EASY_HOMES);

        try {
            Files.createDirectories(easyHomes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return easyHomes;
    }
}
