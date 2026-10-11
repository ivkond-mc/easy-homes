package ivkond.mc.mods.eh.storage;

import ivkond.mc.mods.eh.domain.HomeLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.Duration;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Cooldown and home-limit queries of {@link HomeRepository}. These read the
 * MidnightLib-backed {@code EasyHomesConfig} defaults (cooldown = 5 s,
 * maxHomes = 5), so they double as a probe that the config class is loadable
 * on a plain JVM test classpath.
 */
class HomeRepositoryCooldownTest {
    private static final HomeLocation BASE =
            new HomeLocation("minecraft:overworld", 0.0, 64.0, 0.0, 0.0f, 0.0f);

    @TempDir
    Path dataDir;

    private HomeRepository repository;

    @BeforeEach
    void setUp() {
        repository = new HomeRepository();
        repository.init(dataDir);
    }

    @Test
    void cooldownIsZeroForUnknownPlayer() {
        assertEquals(Duration.ZERO, repository.getCooldown("unknown"));
    }

    @Test
    void cooldownIsZeroBeforeFirstTeleportation() {
        repository.loadConfig("p1");

        assertEquals(Duration.ZERO, repository.getCooldown("p1"));
    }

    @Test
    void cooldownIsPositiveRightAfterTeleportation() {
        repository.onTeleported("p1", "base");

        Duration remaining = repository.getCooldown("p1");

        assertFalse(remaining.isNegative());
        assertTrue(remaining.compareTo(Duration.ofSeconds(5)) <= 0);
        // Fresh teleportation leaves (almost) the whole default cooldown
        assertTrue(remaining.compareTo(Duration.ofSeconds(1)) > 0);
    }

    @Test
    void expiredCooldownReportsZero() {
        repository.onTeleported("p1", "base");
        backdateLastTeleportation("p1", 60);

        assertEquals(Duration.ZERO, repository.getCooldown("p1"));
    }

    @Test
    void clockJumpBackwardsSelfHealsInsteadOfLockingOut() {
        repository.onTeleported("p1", "base");
        OffsetDateTime skewed = OffsetDateTime.now().plusHours(1);
        repository.getHomes("p1").setLastTeleportation(skewed);

        assertEquals(Duration.ZERO, repository.getCooldown("p1"));

        // The heal rewrote the persisted timestamp to roughly "cooldown elapsed"
        OffsetDateTime healed = repository.getHomes("p1").getLastTeleportation();
        assertNotNull(healed);
        assertTrue(Duration.between(healed, OffsetDateTime.now()).abs().compareTo(Duration.ofMinutes(1)) < 0);
    }

    @Test
    void maxHomesNotReachedBelowLimit() {
        repository.setHome("p1", "h1", BASE);
        repository.setHome("p1", "h2", BASE);
        repository.setHome("p1", "h3", BASE);
        repository.setHome("p1", "h4", BASE);

        assertFalse(repository.isMaxHomesReached("p1"));
    }

    @Test
    void maxHomesReachedAtLimit() {
        for (int i = 1; i <= 5; i++) {
            repository.setHome("p1", "h" + i, BASE);
        }

        assertTrue(repository.isMaxHomesReached("p1"));
    }

    @Test
    void maxHomesNotReachedForUnknownPlayer() {
        assertFalse(repository.isMaxHomesReached("unknown"));
    }

    private void backdateLastTeleportation(String playerId, long secondsAgo) {
        repository.getHomes(playerId).setLastTeleportation(OffsetDateTime.now().minusSeconds(secondsAgo));
    }
}
