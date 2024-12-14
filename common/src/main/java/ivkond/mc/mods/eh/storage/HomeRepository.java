package ivkond.mc.mods.eh.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ivkond.mc.mods.eh.config.EasyHomesConfig;
import ivkond.mc.mods.eh.domain.HomeLocation;
import ivkond.mc.mods.eh.domain.PlayerHomes;
import ivkond.mc.mods.eh.utils.Log;
import ivkond.mc.mods.eh.utils.OffsetDateTimeGsonAdapter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.concurrent.ConcurrentHashMap;

public class HomeRepository {
    public static final HomeRepository INSTANCE = new HomeRepository();

    private static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .registerTypeAdapter(OffsetDateTime.class, OffsetDateTimeGsonAdapter.INSTANCE)
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
        return homes.findHome(homeName);
    }

    public void setHome(String playerId, String name, HomeLocation home) {
        PlayerHomes homes = getOrCreateHomes(playerId);
        homes.setHome(name, home);
    }

    public void deleteHome(String playerId, String homeName) {
        PlayerHomes homes = getOrCreateHomes(playerId);
        homes.removeHome(homeName);
    }

    public void loadPlayerConfig(String playerId) {
        Path configPath = getConfigPath(playerId);
        try (BufferedReader reader = Files.newBufferedReader(configPath)) {
            PlayerHomes homes = GSON.fromJson(reader, PlayerHomes.class);
            if (homes == null) {
                Log.error("Malformed player {} homes config file", playerId);
                return;
            }
            DATA.put(playerId, homes);
        } catch (NoSuchFileException e) {
            DATA.put(playerId, new PlayerHomes());
        } catch (IOException e) {
            Log.error("Unable to load player {} homes", playerId, e);
            throw new RuntimeException(e);
        }
    }

    public void unloadPlayerConfig(String playerId) {
        Path configPath = getConfigPath(playerId);
        try (BufferedWriter writer = Files.newBufferedWriter(configPath)) {
            PlayerHomes playerHomes = DATA.get(playerId);
            GSON.toJson(playerHomes, writer);
        } catch (IOException e) {
            Log.error("Unable to save player {} homes", playerId, e);
            throw new RuntimeException(e);
        }
        DATA.remove(playerId);
    }

    public boolean exists(String playerId, String homeName) {
        return DATA.get(playerId).findHome(homeName) != null;
    }

    public Duration getCooldown(String playerId) {
        PlayerHomes homes = getHomes(playerId);
        if (homes.getLastTeleportation() != null) {
            OffsetDateTime expiresAt = homes.getLastTeleportation().plusSeconds(EasyHomesConfig.cooldown);
            return Duration.between(OffsetDateTime.now(), expiresAt);
        }
        return Duration.ZERO;
    }

    public void updateLockDuration(String playerId) {
        PlayerHomes homes = getHomes(playerId);
        homes.setLastTeleportation(OffsetDateTime.now());
    }

    public boolean isMaxHomesReached(String playerId) {
        PlayerHomes homes = getHomes(playerId);
        return homes.getAllHomes().size() >= EasyHomesConfig.maxHomes;
    }

    private Path getConfigPath(String playerId) {
        return dataDir.resolve(playerId + ".json");
    }

    private PlayerHomes getOrCreateHomes(String playerId) {
        return DATA.computeIfAbsent(playerId, id -> new PlayerHomes());
    }
}
