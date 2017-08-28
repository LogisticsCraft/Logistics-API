package com.logisticscraft.logisticsexample;

import com.logisticscraft.logisticsloader.LogisticsLoader;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author JARvis (Пётр) PROgrammer
 */
public final class LogisticsExamplePlugin extends JavaPlugin {
    private static LogisticsExamplePlugin instance;

    public static LogisticsExamplePlugin getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        LogisticsLoader.load();
    }
}
