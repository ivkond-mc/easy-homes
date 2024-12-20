package ivkond.mc.mods.eh.config;

import eu.midnightdust.lib.config.MidnightConfig;

public class EasyHomesConfig extends MidnightConfig {
    public static final String DEFAULT_HOME = "main";
    public static final String MAIN = "main";

    @Entry(category = MAIN, min = 1)
    public static int cooldown = 5;
    @Entry(category = MAIN, min = 1)
    public static int maxHomes = 5;
}
