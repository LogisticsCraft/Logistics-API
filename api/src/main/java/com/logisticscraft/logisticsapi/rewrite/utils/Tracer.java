package com.logisticscraft.logisticsapi.rewrite.utils;

import lombok.Setter;
import org.bukkit.ChatColor;

import java.util.logging.Logger;

public final class Tracer {

    @Setter
    private static boolean debug;
    @Setter
    private static Logger logger;

    public static void info(String... messages) {
        for (String message : messages) {
            logger.info(message);
        }
    }

    public static void warn(String... messages) {
        for (String message : messages) {
            logger.warning(message);
        }
    }

    public static void err(String... messages) {
        for (String message : messages) {
            logger.severe(ChatColor.DARK_RED + message);
        }
    }

    public static void debug(String... messages) {
        if (debug) {
            for (String message : messages) {
                logger.info(ChatColor.YELLOW + "Debug: " + ChatColor.ITALIC + message);
            }
        }
    }

}
