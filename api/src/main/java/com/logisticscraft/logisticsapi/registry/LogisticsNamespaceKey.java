package com.logisticscraft.logisticsapi.registry;

import com.logisticscraft.logisticsapi.annotation.ApiComponent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author JARvis (Пётр) PROgrammer
 */
public class LogisticsNamespaceKey {
    private JavaPlugin plugin;
    private String name;

    @ApiComponent
    public LogisticsNamespaceKey(JavaPlugin plugin, String name) {
        this.plugin = plugin;
        this.name = name;
    }

    @ApiComponent
    public JavaPlugin getPlugin() {
        return plugin;
    }

    @ApiComponent
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return plugin.getName() + ":" + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LogisticsNamespaceKey that = (LogisticsNamespaceKey) o;

        if (plugin != null ? !plugin.equals(that.plugin) : that.plugin != null) return false;
        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        int result = plugin != null ? plugin.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
