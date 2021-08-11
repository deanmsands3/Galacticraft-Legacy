package micdoodle8.mods.galacticraft.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import micdoodle8.mods.galacticraft.Constants;

public class GCLog
{

    private static Logger log = LogManager.getFormatterLogger(Constants.MOD_NAME_SIMPLE);

    public static void info(String message)
    {
        log.info(message);
    }

    public static void warn(String message)
    {
    	log.warn(message);
    }

    public static void warn(Exception e, String message)
    {
    	log.warn(message, e);
    }

    public static void err(String message)
    {
        log.error(message);
    }

    public static void debug(String message)
    {
        if (ConfigManagerCore.enableDebug)
        {
            log.debug(message);
        }
    }

    public static void exception(Exception e)
    {
        log.catching(e);
    }
}
