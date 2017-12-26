package com.logisticscraft.logisticsapi.utils;

import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

import java.util.logging.Logger;

@UtilityClass
public final class Tracer {

    @Setter
    private boolean debug;
    @Setter
    private Logger logger;

    public void info(@NonNull String... messages) {
        for (String message : messages) {
            logger.info(message);
        }
    }

    public void warn(@NonNull String... messages) {
        for (String message : messages) {
            logger.warning(message);
        }
    }

    public void err(@NonNull String... messages) {
        for (String message : messages) {
            logger.severe(ChatColor.DARK_RED + message);
        }
    }

    public void debug(@NonNull String... messages) {
        if (debug) {
            for (String message : messages) {
                logger.info(ChatColor.YELLOW + "Debug: " + ChatColor.ITALIC + message);
            }
        }
    }

}
