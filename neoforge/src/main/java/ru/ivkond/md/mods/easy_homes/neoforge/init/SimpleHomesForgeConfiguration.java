package ru.ivkond.md.mods.easy_homes.neoforge.init;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import ru.ivkond.md.mods.easy_homes.SimpleHomesConfiguration;

public class SimpleHomesForgeConfiguration implements SimpleHomesConfiguration {
    public static final SimpleHomesForgeConfiguration CONFIG;
    public static final ModConfigSpec CONFIG_SPEC;

    private final ModConfigSpec.IntValue delay;
    private final ModConfigSpec.IntValue maxHomes;

    private SimpleHomesForgeConfiguration(ModConfigSpec.Builder builder) {
        delay = builder.defineInRange("delay", 1, 1, Integer.MAX_VALUE);
        maxHomes = builder.defineInRange("max_homes", 20, 1, Integer.MAX_VALUE);
    }

    @Override
    public int getDelay() {
        return delay.getAsInt();
    }

    @Override
    public int getMaxHomes() {
        return maxHomes.getAsInt();
    }

    static {
        Pair<SimpleHomesForgeConfiguration, ModConfigSpec> pair =
                new ModConfigSpec.Builder().configure(SimpleHomesForgeConfiguration::new);

        //Store the resulting values
        CONFIG = pair.getLeft();
        CONFIG_SPEC = pair.getRight();
    }
}
