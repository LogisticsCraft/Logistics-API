package com.logisticscraft.logisticsapi.data;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.bukkit.plugin.Plugin;

import java.util.Optional;

@Value
@AllArgsConstructor
public class LogisticKey {

    private String namespace;
    private String name;

    public LogisticKey(Plugin plugin, String name) {
        this.namespace = plugin.getName();
        this.name = name;
    }

    public Optional<Plugin> getPlugin() {
        return new SafePlugin(namespace).getPlugin();
    }

    @Override
    public String toString() {
        return namespace + ":" + name;
    }

}
