package com.logisticscraft.logisticsapi.utils;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

import java.util.logging.Logger;

@UtilityClass
public final class Tracer {

    @Setter
    @Getter
    private boolean debug = false;
    @Setter
    private Logger logger;

    public static void info(@NonNull String... messages) {
        for (String message : messages) {
            logger.info(message);
        }
    }

    public static void warn(@NonNull String... messages) {
        for (String message : messages) {
            logger.warning(message);
        }
    }

    public static void error(@NonNull String... messages) {
        for (String message : messages) {
            logger.severe(ChatColor.DARK_RED + message);
        }
    }

    public static void debug(@NonNull String... messages) {
        if (debug) {
            for (String message : messages) {
                logger.info(ChatColor.YELLOW + "Debug: " + ChatColor.ITALIC + message);
            }
        }
    }
}
