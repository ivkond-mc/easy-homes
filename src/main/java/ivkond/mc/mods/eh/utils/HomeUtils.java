package ivkond.mc.mods.eh.utils;

import java.util.regex.Pattern;

public class HomeUtils {
    private static final int MAX_NAME_LENGTH = 64;
    private static final Pattern HOME_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]+$");

    public static boolean isInvalidName(String name) {
        return name == null || name.length() > MAX_NAME_LENGTH || !HOME_PATTERN.matcher(name).matches();
    }
}
