package ivkond.mc.mods.eh.config;

import eu.midnightdust.lib.config.MidnightConfig;

public class EasyHomesConfig extends MidnightConfig {
    public static final String DEFAULT_HOME = "main";
    private static final String CONFIG_CATEGORY = "main";

    @Entry(category = CONFIG_CATEGORY, min = 1)
    public static int cooldown = 5;
    @Entry(category = CONFIG_CATEGORY, min = 1)
    public static int maxHomes = 5;
}
