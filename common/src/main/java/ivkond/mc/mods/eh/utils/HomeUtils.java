package ivkond.mc.mods.eh.utils;

import com.mojang.brigadier.StringReader;

public class HomeUtils {
    public static boolean isInvalidName(String name) {
        for (char c : name.toCharArray()) {
            if (!StringReader.isAllowedInUnquotedString(c)) {
                return true;
            }
        }
        return false;
    }
}
