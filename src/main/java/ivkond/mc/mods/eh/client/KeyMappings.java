package ivkond.mc.mods.eh.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
//? if >=1.21.9 {
import net.minecraft.resources.Identifier;
//? }

public class KeyMappings {
    //? if <1.21.9 {
    /*private static final String CATEGORY = "easy_homes.key.category";
    *///? } else {
    private static final KeyMapping.Category CATEGORY = new KeyMapping.Category(Identifier.parse("easy_homes:key_category"));
    //? }

    public static final KeyMapping TP_TO_DEFAULT_HOME = new KeyMapping(
            "easy_homes.key.tp_home",
            //? if >=26.3 {
            /*InputConstants.Type.KEYBOARD,*/
            //? } else {
            InputConstants.Type.KEYSYM,
            //? }
            InputConstants.KEY_H,
            CATEGORY
    );
    public static final KeyMapping SET_NEW_HOME = new KeyMapping(
            "easy_homes.key.set_new_home",
            //? if >=26.3 {
            /*InputConstants.Type.KEYBOARD,*/
            //? } else {
            InputConstants.Type.KEYSYM,
            //? }
            InputConstants.KEY_J,
            CATEGORY
    );
}
