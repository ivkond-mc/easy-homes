package ivkond.mc.mods.eh.storage;

import ivkond.mc.mods.eh.domain.HomeLocation;
import ivkond.mc.mods.eh.domain.PlayerHomes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HomeRepositoryTest {
    private static final HomeLocation BASE =
            new HomeLocation("minecraft:overworld", 1.5, 64.0, -2.25, 30.0f, -170.5f);
    private static final HomeLocation FARM =
            new HomeLocation("minecraft:overworld", 100.0, 65.0, 200.0, 0.0f, 90.0f);

    @TempDir
    Path dataDir;

    private HomeRepository repository;

    @BeforeEach
    void setUp() {
        repository = new HomeRepository();
        repository.init(dataDir);
    }

    @Test
    void loadConfigOfUnknownPlayerCreatesEmptyState() {
        repository.loadConfig("p1");

        PlayerHomes homes = repository.getHomes("p1");
        assertNotNull(homes);
        assertTrue(homes.getAllHomes().isEmpty());
        assertFalse(Files.exists(dataDir.resolve("p1.json")));
    }

    @Test
    void setHomeWritesJsonFileWithLocation() {
        repository.setHome("p1", "base", BASE);

        Path file = dataDir.resolve("p1.json");
        assertTrue(Files.exists(file));
        assertEquals(BASE, repository.findHome("p1", "base"));
    }

    @Test
    void savedHomesSurviveForgetAndReload() throws IOException {
        repository.setHome("p1", "base", BASE);
        repository.setHome("p1", "farm", FARM);
        assertTrue(fileHasHome("p1", "base"));

        repository.forgetConfig("p1");
        assertNull(repository.getHomes("p1"));

        repository.loadConfig("p1");
        assertEquals(BASE, repository.findHome("p1", "base"));
        assertEquals(FARM, repository.findHome("p1", "farm"));
    }

    @Test
    void setHomeOverwritesExistingHome() {
        repository.setHome("p1", "base", BASE);
        repository.setHome("p1", "base", FARM);

        assertEquals(FARM, repository.findHome("p1", "base"));
        assertEquals(1, repository.getHomes("p1").getAllHomes().size());
    }

    @Test
    void renameHomeMovesLocationAndPersists() {
        repository.setHome("p1", "base", BASE);

        repository.renameHome("p1", "base", "castle");

        assertNull(repository.findHome("p1", "base"));
        assertEquals(BASE, repository.findHome("p1", "castle"));
        assertEquals(1, repository.getHomes("p1").getAllHomes().size());

        repository.forgetConfig("p1");
        repository.loadConfig("p1");
        assertEquals(BASE, repository.findHome("p1", "castle"));
    }

    @Test
    void renameUnknownHomeIsNoOpWithoutFileWrite() {
        repository.renameHome("p1", "missing", "new-name");

        assertFalse(Files.exists(dataDir.resolve("p1.json")));
    }

    @Test
    void deleteHomeRemovesItAndPersists() {
        repository.setHome("p1", "base", BASE);

        repository.deleteHome("p1", "base");

        assertNull(repository.findHome("p1", "base"));
        repository.forgetConfig("p1");
        repository.loadConfig("p1");
        assertNull(repository.findHome("p1", "base"));
    }

    @Test
    void deleteUnknownHomeIsSafe() {
        repository.setHome("p1", "base", BASE);

        repository.deleteHome("p1", "missing");

        assertEquals(BASE, repository.findHome("p1", "base"));
    }

    @Test
    void existsChecksBothPlayerAndHome() {
        repository.setHome("p1", "base", BASE);

        assertTrue(repository.exists("p1", "base"));
        assertFalse(repository.exists("p1", "missing"));
        assertFalse(repository.exists("unknown", "base"));
    }

    @Test
    void getLastVisitedHomeOfUnknownPlayerIsNull() {
        assertNull(repository.getLastVisitedHome("unknown"));
    }

    @Test
    void onTeleportedRecordsVisitAndPersistsTeleportationTime() {
        repository.onTeleported("p1", "base");
        assertNotNull(repository.getHomes("p1").getLastTeleportation());

        // First visit has no previous home yet; the second one promotes "base"
        assertNull(repository.getLastVisitedHome("p1"));
        repository.onTeleported("p1", "farm");
        assertEquals("base", repository.getLastVisitedHome("p1"));

        repository.forgetConfig("p1");
        repository.loadConfig("p1");
        assertNotNull(repository.getHomes("p1").getLastTeleportation());
    }

    @Test
    void nullConfigContentLeavesPlayerStateUnset() throws IOException {
        Files.writeString(dataDir.resolve("p1.json"), "null");

        repository.loadConfig("p1");

        assertNull(repository.getHomes("p1"));
    }

    @Test
    void forgetAllClearsEveryPlayer() {
        repository.setHome("p1", "base", BASE);
        repository.setHome("p2", "farm", FARM);

        repository.forgetAll();

        assertNull(repository.getHomes("p1"));
        assertNull(repository.getHomes("p2"));
    }

    private boolean fileHasHome(String playerId, String homeName) throws IOException {
        return Files.readString(dataDir.resolve(playerId + ".json")).contains("\"" + homeName + "\"");
    }
}
