package ivkond.mc.mods.eh.utils;

import java.time.Duration;

/**
 * Pure duration arithmetic, kept free of Minecraft types so it is unit-testable
 * on a plain JVM.
 */
public final class Durations {
    private static final long SECONDS_PER_DAY = 86400;
    private static final long SECONDS_PER_HOUR = 3600;
    private static final long SECONDS_PER_MINUTE = 60;

    private Durations() {
    }

    /**
     * Splits a duration into day/hour/minute/second buckets, e.g.
     * {@code PT25H3M4S} into {@code [1, 1, 3, 4]}. A "day" is a fixed 24-hour
     * unit, not a calendar day.
     *
     * @return {@code {days, hours, minutes, seconds}}; all zeros for durations
     * shorter than one second (and, mirroring {@link Duration#getSeconds()},
     * floor-division components for negative durations)
     */
    public static long[] breakdown(Duration duration) {
        long seconds = duration.getSeconds();
        return new long[]{
                seconds / SECONDS_PER_DAY,
                (seconds % SECONDS_PER_DAY) / SECONDS_PER_HOUR,
                (seconds % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE,
                seconds % SECONDS_PER_MINUTE
        };
    }
}
