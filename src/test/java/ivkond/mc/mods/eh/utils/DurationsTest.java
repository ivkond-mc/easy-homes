package ivkond.mc.mods.eh.utils;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class DurationsTest {
    @Test
    void zeroBreaksDownToAllZeros() {
        assertArrayEquals(new long[]{0, 0, 0, 0}, Durations.breakdown(Duration.ZERO));
    }

    @Test
    void subSecondDurationBreaksDownToAllZeros() {
        assertArrayEquals(new long[]{0, 0, 0, 0}, Durations.breakdown(Duration.ofMillis(500)));
        assertArrayEquals(new long[]{0, 0, 0, 0}, Durations.breakdown(Duration.ofNanos(999_999_999)));
    }

    @Test
    void subMinuteDurationStaysInSeconds() {
        assertArrayEquals(new long[]{0, 0, 0, 59}, Durations.breakdown(Duration.ofSeconds(59)));
        assertArrayEquals(new long[]{0, 0, 0, 1}, Durations.breakdown(Duration.ofSeconds(1)));
    }

    @Test
    void exactMinuteCarries() {
        assertArrayEquals(new long[]{0, 0, 1, 0}, Durations.breakdown(Duration.ofSeconds(60)));
    }

    @Test
    void minuteSecondBoundary() {
        assertArrayEquals(new long[]{0, 0, 59, 59}, Durations.breakdown(Duration.ofSeconds(3599)));
    }

    @Test
    void exactHourBreaksDownCleanly() {
        assertArrayEquals(new long[]{0, 1, 0, 0}, Durations.breakdown(Duration.ofHours(1)));
    }

    @Test
    void secondBelowFullDay() {
        assertArrayEquals(new long[]{0, 23, 59, 59}, Durations.breakdown(Duration.ofSeconds(86399)));
    }

    @Test
    void exactDayBreaksDownCleanly() {
        assertArrayEquals(new long[]{1, 0, 0, 0}, Durations.breakdown(Duration.ofDays(1)));
    }

    @Test
    void mixedDurationSplitsIntoEveryUnit() {
        // 1d 2h 3m 4s
        assertArrayEquals(new long[]{1, 2, 3, 4}, Durations.breakdown(Duration.ofSeconds(93784)));
    }

    @Test
    void multipleDays() {
        assertArrayEquals(new long[]{3, 0, 0, 0}, Durations.breakdown(Duration.ofDays(3)));
        // 2d 23h 59m 59s, one second short of 3 days
        assertArrayEquals(new long[]{2, 23, 59, 59}, Durations.breakdown(Duration.ofDays(3).minusSeconds(1)));
    }

    @Test
    void maxCooldownConfigValue() {
        // Upper bound of the config cooldown (86400 s = 1 day)
        assertArrayEquals(new long[]{1, 0, 0, 0}, Durations.breakdown(Duration.ofSeconds(86400)));
    }

    @Test
    void hugeDurationDoesNotOverflow() {
        assertArrayEquals(new long[]{106751991167300L, 15, 30, 7}, Durations.breakdown(Duration.ofSeconds(Long.MAX_VALUE)));
    }

    @Test
    void negativeDurationsUseFloorDivision() {
        // Documents the pre-existing arithmetic: negative values break down with
        // floor-division components, not sign-magnitude ones
        assertArrayEquals(new long[]{0, 0, 0, -1}, Durations.breakdown(Duration.ofSeconds(-1)));
        assertArrayEquals(new long[]{0, 0, -1, -30}, Durations.breakdown(Duration.ofSeconds(-90)));
        assertArrayEquals(new long[]{0, -1, -1, -1}, Durations.breakdown(Duration.ofSeconds(-3661)));
        assertArrayEquals(new long[]{0, -23, -59, -59}, Durations.breakdown(Duration.ofSeconds(-86399)));
    }
}
