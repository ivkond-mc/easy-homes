package ru.ivkond.md.mods.easy_homes.domain;

public record PlayerHome(
        String name,
        String dimension,
        int x,
        int y,
        int z
) {

    public String coordinates() {
        return x + "," + y + "," + z;
    }
}
