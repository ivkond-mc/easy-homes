package ivkond.mc.mods.eh.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HomeLocationTest {
    @Test
    void coordinatesFormatWholeNumbersWithoutDecimals() {
        HomeLocation location = new HomeLocation("minecraft:overworld", 1.0, 2.0, 3.0, 0.0f, 0.0f);

        assertEquals("[1;2;3]", location.coordinates());
    }

    @Test
    void coordinatesRoundFractionsHalfUp() {
        HomeLocation location = new HomeLocation("minecraft:overworld", 1.4, 2.5, 3.6, 0.0f, 0.0f);

        assertEquals("[1;3;4]", location.coordinates());
    }

    @Test
    void coordinatesKeepNegativeSign() {
        HomeLocation location = new HomeLocation("minecraft:the_nether", -3.0, 0.0, 64.0, 0.0f, 0.0f);

        assertEquals("[-3;0;64]", location.coordinates());
    }

    @Test
    void recordEqualityCoversAllComponents() {
        HomeLocation location = new HomeLocation("minecraft:overworld", 1.0, 2.0, 3.0, 10.0f, 20.0f);

        assertEquals(location, new HomeLocation("minecraft:overworld", 1.0, 2.0, 3.0, 10.0f, 20.0f));
        assertEquals(location.dimension(), "minecraft:overworld");
        assertEquals(location.x(), 1.0);
        assertEquals(location.rotY(), 20.0f);
    }
}
