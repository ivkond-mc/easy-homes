package ivkond.mc.mods.eh.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ivkond.mc.mods.eh.EasyHomesMod;

public class Log {
    public static final Logger logger = LogManager.getLogger(EasyHomesMod.MOD_ID);

    public static void info(String message) {
        logger.info(message);
    }

    public static void info(String message, Object... args) {
        logger.info(message, args);
    }

    public static void error(String message, Object... args) {
        logger.error(message, args);
    }

    public static void debug(String message, Object... args) {
        logger.debug(message, args);
    }
}
