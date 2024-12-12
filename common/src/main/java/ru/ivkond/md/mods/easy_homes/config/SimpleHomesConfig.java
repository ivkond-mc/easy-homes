package ru.ivkond.md.mods.easy_homes.config;

import eu.midnightdust.lib.config.MidnightConfig;

public class SimpleHomesConfig extends MidnightConfig {
    public static final String MAIN = "main";

    @Entry(category = MAIN, min = 1)
    public static int delay = 1;
    @Entry(category = MAIN, min = 1)
    public static int maxHomes = 5;
}
