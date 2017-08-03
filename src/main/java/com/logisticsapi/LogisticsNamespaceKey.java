package com.logisticsapi;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author JARvis (Пётр) PROgrammer
 * TODO
 */
public class LogisticsNamespaceKey {
    private JavaPlugin plugin;
    private String name;

    public LogisticsNamespaceKey(JavaPlugin plugin, String name) {
        this.plugin = plugin;
        this.name = name;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return plugin.getName() + ":" + name;
    }
}
