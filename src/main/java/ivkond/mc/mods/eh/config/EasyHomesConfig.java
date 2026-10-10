package ivkond.mc.mods.eh.config;

import eu.midnightdust.lib.config.MidnightConfig;

public class EasyHomesConfig extends MidnightConfig {
    public static final String DEFAULT_HOME = "main";
    private static final String CONFIG_CATEGORY = "main";

    //? if <1.19.3 {
    /*// MidnightConfig @Entry has no category attribute in the 1.18.2-era release
    @Entry(min = 1)
    public static int cooldown = 5;
    @Entry(min = 1)
    public static int maxHomes = 5;*/
    //? } else {
    @Entry(category = CONFIG_CATEGORY, min = 1)
    public static int cooldown = 5;
    @Entry(category = CONFIG_CATEGORY, min = 1)
    public static int maxHomes = 5;
    //? }
}
