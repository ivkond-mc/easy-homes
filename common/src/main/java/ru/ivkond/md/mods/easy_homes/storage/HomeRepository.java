package ru.ivkond.md.mods.easy_homes.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.ivkond.md.mods.easy_homes.domain.HomeLocation;
import ru.ivkond.md.mods.easy_homes.domain.PlayerHomes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentHashMap;

public class HomeRepository {
    public static final HomeRepository INSTANCE = new HomeRepository();

    private static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();

    // Map<Player, Homes>
    private final ConcurrentHashMap<String, PlayerHomes> DATA = new ConcurrentHashMap<>();
    private Path dataDir;

    public void init(Path dataDir) {
        this.dataDir = dataDir;
    }

    public void unload() {
        DATA.keySet().forEach(this::unloadPlayerConfig);
    }

    public PlayerHomes getHomes(String playerId) {
        return DATA.get(playerId);
    }

    public HomeLocation findHome(String playerId, String homeName) {
        PlayerHomes homes = getOrCreateHomes(playerId);
        return homes.find(homeName);
    }

    public void setHome(String playerId, String name, HomeLocation home) {
        PlayerHomes homes = getOrCreateHomes(playerId);
        homes.set(name, home);
    }

    public void deleteHome(String playerId, String homeName) {
        PlayerHomes homes = getOrCreateHomes(playerId);
        homes.remove(homeName);
    }

    public void loadPlayerConfig(String playerId) {
        Path configPath = getConfigPath(playerId);
        try (BufferedReader reader = Files.newBufferedReader(configPath)) {
            PlayerHomes homes = GSON.fromJson(reader, PlayerHomes.class);
            DATA.put(playerId, homes);
        } catch (NoSuchFileException e) {
            DATA.put(playerId, new PlayerHomes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void unloadPlayerConfig(String playerId) {
        Path configPath = getConfigPath(playerId);
        try (BufferedWriter writer = Files.newBufferedWriter(configPath)) {
            PlayerHomes playerHomes = DATA.get(playerId);
            GSON.toJson(playerHomes, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        DATA.remove(playerId);
    }

    private Path getConfigPath(String playerId) {
        return dataDir.resolve(playerId + ".json");
    }

    private PlayerHomes getOrCreateHomes(String playerId) {
        return DATA.computeIfAbsent(playerId, id -> new PlayerHomes());
    }

    public boolean exists(String playerId, String homeName) {
        return DATA.get(playerId).find(homeName) != null;
    }
}
