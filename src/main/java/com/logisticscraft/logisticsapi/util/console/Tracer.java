package com.logisticscraft.logisticsapi.util.console;

import org.bukkit.ChatColor;

import java.util.logging.Logger;

/**
 * Created by PROgrm_JARvis on 14.04.2017.
 */
public final class Tracer {
    //TODO debug from config
    //public static boolean debug;
    private static Logger logger;

    public static void setLogger(Logger logger) {
        Tracer.logger = logger;
    }
    //Tracing

    public static void msg(String... messages) {
        for (String message : messages) logger.info(message);
    }

    /*public static void sql(String message) {
        logger.info("SQL-Info: " + message);
    }*/

    public static void warn(String... messages) {
        for (String message : messages) logger.warning(message);
    }

    public static void err(String... messages) {
        for (String message : messages) logger.warning(ChatColor.DARK_RED + message);
    }

    //TODO open when debug is made to be loaded from config
    /*
    public static void debug(String message) {
        if (debug) logger.info(ChatColor.YELLOW + "Debug-Info: " + ChatColor.ITALIC + message);
    }*/

    public static void traceLogo() {
        msg(ASCII_LOGO);
    }

    public static final String[] ASCII_LOGO =
            {" __                                   __                                 ______  ____    ______     ",
                    "/\\ \\                      __         /\\ \\__  __                         /\\  _  \\/\\  _`\\ /\\__  _\\    ",
                    "\\ \\ \\        ___      __ /\\_\\    ____\\ \\ ,_\\/\\_\\    ___    ____         \\ \\ \\L\\ \\ \\ \\L\\ \\/_/\\ \\/    ",
                    " \\ \\ \\  __  / __`\\  /'_ `\\/\\ \\  /',__\\\\ \\ \\/\\/\\ \\  /'___\\ /',__\\  _______\\ \\  __ \\ \\ ,__/  \\ \\ \\    ",
                    "  \\ \\ \\L\\ \\/\\ \\L\\ \\/\\ \\L\\ \\ \\ \\/\\__, `\\\\ \\ \\_\\ \\ \\/\\ \\__//\\__, `\\/\\______\\\\ \\ \\/\\ \\ \\ \\/    \\_\\ \\__ ",
                    "   \\ \\____/\\ \\____/\\ \\____ \\ \\_\\/\\____/ \\ \\__\\\\ \\_\\ \\____\\/\\____/\\/______/ \\ \\_\\ \\_\\ \\_\\    /\\_____\\",
                    "    \\/___/  \\/___/  \\/___L\\ \\/_/\\/___/   \\/__/ \\/_/\\/____/\\/___/            \\/_/\\/_/\\/_/    \\/_____/",
                    "                      /\\____/                                                                       ",
                    "                      \\_/__/                                                                        ",
                    "                                                                                                    "};
}
