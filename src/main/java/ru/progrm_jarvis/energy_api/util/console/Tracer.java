package ru.progrm_jarvis.energy_api.util.console;

import org.bukkit.ChatColor;

import java.util.logging.Logger;

/**
 * Created by PROgrm_JARvis on 14.04.2017.
 */
public final class Tracer {
    //public static boolean debug;
    private static Logger logger;

    public static void setLogger(Logger logger) {
        Tracer.logger = logger;
    }
    //Tracing

    public static void msg(String message) {
        logger.info(message);
    }

    /*public static void sql(String message) {
        logger.info("SQL-Info: " + message);
    }*/

    public static void warn(String message) {
        logger.warning(message);
    }

    public static void err(String message) {
        logger.warning(ChatColor.DARK_RED + message);
    }

    /*
    public static void debug(String message) {
        if (debug) logger.info(ChatColor.YELLOW + "Debug-Info: " + ChatColor.ITALIC + message);
    }*/
}
