package ivkond.mc.mods.eh.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HomeUtilsTest {
    @Test
    void nullNameIsInvalid() {
        assertTrue(HomeUtils.isInvalidName(null));
    }

    @Test
    void emptyNameIsInvalid() {
        // Regression guard: empty names must not pass validation
        assertTrue(HomeUtils.isInvalidName(""));
    }

    @Test
    void blankNameIsInvalid() {
        assertTrue(HomeUtils.isInvalidName(" "));
        assertTrue(HomeUtils.isInvalidName("\t"));
    }

    @Test
    void alphanumericNameIsValid() {
        assertFalse(HomeUtils.isInvalidName("home"));
        assertFalse(HomeUtils.isInvalidName("a"));
        assertFalse(HomeUtils.isInvalidName("123"));
    }

    @Test
    void underscoreAndHyphenAreValid() {
        assertFalse(HomeUtils.isInvalidName("Home_1"));
        assertFalse(HomeUtils.isInvalidName("my-home"));
        assertFalse(HomeUtils.isInvalidName("h_o-m1"));
    }

    @Test
    void maxBoundaryLengthIsValid() {
        assertFalse(HomeUtils.isInvalidName("a".repeat(64)));
    }

    @Test
    void overlyLongNameIsInvalid() {
        assertTrue(HomeUtils.isInvalidName("a".repeat(65)));
    }

    @Test
    void specialCharactersAreInvalid() {
        assertTrue(HomeUtils.isInvalidName("home!"));
        assertTrue(HomeUtils.isInvalidName("home name"));
        assertTrue(HomeUtils.isInvalidName("home/name"));
        assertTrue(HomeUtils.isInvalidName("home.name"));
        assertTrue(HomeUtils.isInvalidName("дом"));
        assertTrue(HomeUtils.isInvalidName("home\n"));
    }
}
