package ru.ivkond.md.mods.easy_homes.domain;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public record HomeLocation(
        ResourceKey<Level> dimension,
        double x,
        double y,
        double z,
        float rotX,
        float rotY
) {

    public String dimensionName() {
        return dimension.location().getPath();
    }

    public String coordinates() {
        return String.format("[%.0f;%.0f;%.0f]", x, y, z);
    }
}
