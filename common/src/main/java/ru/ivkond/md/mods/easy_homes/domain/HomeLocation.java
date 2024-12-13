package ru.ivkond.md.mods.easy_homes.domain;

public record HomeLocation(
        String dimension,
        double x,
        double y,
        double z,
        float rotX,
        float rotY
) {
    public String coordinates() {
        return String.format("[%.0f;%.0f;%.0f]", x, y, z);
    }
}
