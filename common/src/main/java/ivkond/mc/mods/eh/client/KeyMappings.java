package ivkond.mc.mods.eh.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

public class KeyMappings {
    private static final String CATEGORY = "easy_homes.key.category";

    public static final KeyMapping TP_TO_DEFAULT_HOME = new KeyMapping(
            "easy_homes.key.tp_home",
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_H,
            CATEGORY
    );
    public static final KeyMapping SET_NEW_HOME = new KeyMapping(
            "easy_homes.key.set_new_home",
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_J,
            CATEGORY
    );
}
