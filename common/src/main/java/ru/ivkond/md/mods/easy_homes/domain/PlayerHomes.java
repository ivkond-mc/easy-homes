package ru.ivkond.md.mods.easy_homes.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PlayerHomes {
    // Map<Name, Location>
    private final Map<String, HomeLocation> homes = new HashMap<>();

    public void remove(String name) {
        homes.remove(name);
    }

    public HomeLocation find(String name) {
        return homes.get(name);
    }

    public void set(String name, HomeLocation home) {
        homes.put(name, home);
    }

    public Map<String, HomeLocation> getAll() {
        return Collections.unmodifiableMap(homes);
    }
}
