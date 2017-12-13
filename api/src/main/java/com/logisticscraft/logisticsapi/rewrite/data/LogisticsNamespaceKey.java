package com.logisticscraft.logisticsapi.rewrite.data;

import com.logisticscraft.logisticsapi.annotation.ApiComponent;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

@Value
public class LogisticsNamespaceKey {

    private Plugin plugin;
    private String name;

    @Override
    public String toString() {
        new LogisticsNamespaceKey()
        return plugin.getName() + ":" + name;
    }

}
