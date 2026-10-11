package ivkond.mc.mods.eh.domain;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlayerHomesTest {
    private static final HomeLocation BASE =
            new HomeLocation("minecraft:overworld", 0.0, 64.0, 0.0, 0.0f, 0.0f);
    private static final HomeLocation FARM =
            new HomeLocation("minecraft:overworld", 100.0, 65.0, -200.0, 30.0f, 180.0f);

    @Test
    void newPlayerHasNoHomesAndNoState() {
        PlayerHomes homes = new PlayerHomes();

        assertTrue(homes.getAllHomes().isEmpty());
        assertNull(homes.findHome("base"));
        assertNull(homes.getLastVisitedHome());
        assertNull(homes.getLastTeleportation());
    }

    @Test
    void setHomeStoresAndFindsByName() {
        PlayerHomes homes = new PlayerHomes();
        homes.setHome("base", BASE);

        assertEquals(BASE, homes.findHome("base"));
        assertNull(homes.findHome("farm"));
        assertEquals(1, homes.getAllHomes().size());
    }

    @Test
    void setHomeWithSameNameReplacesLocation() {
        PlayerHomes homes = new PlayerHomes();
        homes.setHome("base", BASE);
        homes.setHome("base", FARM);

        assertEquals(FARM, homes.findHome("base"));
        assertEquals(1, homes.getAllHomes().size());
    }

    @Test
    void removeHomeDeletesIt() {
        PlayerHomes homes = new PlayerHomes();
        homes.setHome("base", BASE);

        homes.removeHome("base");

        assertNull(homes.findHome("base"));
        assertTrue(homes.getAllHomes().isEmpty());
    }

    @Test
    void removeUnknownHomeIsNoOp() {
        PlayerHomes homes = new PlayerHomes();
        homes.setHome("base", BASE);

        homes.removeHome("farm");

        assertEquals(BASE, homes.findHome("base"));
    }

    @Test
    void getAllHomesIsUnmodifiable() {
        PlayerHomes homes = new PlayerHomes();
        homes.setHome("base", BASE);

        Map<String, HomeLocation> all = homes.getAllHomes();
        assertThrows(UnsupportedOperationException.class, () -> all.put("farm", FARM));
    }

    @Test
    void lastVisitedHomeStartsAsNull() {
        PlayerHomes homes = new PlayerHomes();

        homes.setLastVisitedHome("base");

        assertNull(homes.getLastVisitedHome());
    }

    @Test
    void lastVisitedHomeTracksPreviousDistinctHome() {
        PlayerHomes homes = new PlayerHomes();
        homes.setLastVisitedHome("base");

        homes.setLastVisitedHome("farm");
        assertEquals("base", homes.getLastVisitedHome());

        homes.setLastVisitedHome("mine");
        assertEquals("farm", homes.getLastVisitedHome());
    }

    @Test
    void repeatedVisitToSameHomeKeepsPreviousHome() {
        PlayerHomes homes = new PlayerHomes();
        homes.setLastVisitedHome("base");
        homes.setLastVisitedHome("farm");

        homes.setLastVisitedHome("farm");

        assertEquals("base", homes.getLastVisitedHome());
    }

    @Test
    void returningToPreviousHomeSwapsHistory() {
        PlayerHomes homes = new PlayerHomes();
        homes.setLastVisitedHome("base");
        homes.setLastVisitedHome("farm");

        homes.setLastVisitedHome("base");

        assertEquals("farm", homes.getLastVisitedHome());
    }

    @Test
    void nullVisitIsIgnored() {
        PlayerHomes homes = new PlayerHomes();
        homes.setLastVisitedHome("base");
        homes.setLastVisitedHome("farm");

        homes.setLastVisitedHome(null);

        assertEquals("base", homes.getLastVisitedHome());
    }

    @Test
    void lastTeleportationIsMutable() {
        PlayerHomes homes = new PlayerHomes();
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);

        homes.setLastTeleportation(now);

        assertEquals(now, homes.getLastTeleportation());
    }
}
