package ru.ivkond.md.mods.easy_homes.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

public class KeyMappings {
    public static final KeyMapping TP_TO_DEFAULT_HOME = new KeyMapping(
            "easy_homes.key.tp_home",
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_H,
            "easy_homes.key.category"
    );
}
