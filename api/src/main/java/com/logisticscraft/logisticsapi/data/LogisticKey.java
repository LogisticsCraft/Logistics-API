package com.logisticscraft.logisticsapi.data;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Optional;

@Value
@AllArgsConstructor
public class LogisticKey implements Serializable {

    private String namespace;
    private String name;

    public LogisticKey(@NonNull final Plugin plugin, @NonNull final String name) {
        this.namespace = plugin.getName();
        this.name = name;
    }

    public LogisticKey(@NotNull final String key) {
        if (key.split(":").length == 2) {
            this.namespace = key.split(":")[0];
            this.name = key.split(":")[1];
        } else {
            this.namespace = "Unknown";
            this.name = key.replace(":", "");
        }
    }

    public Optional<Plugin> getPlugin() {
        return new SafePlugin(namespace).getPlugin();
    }

    @Override
    public String toString() {
        return namespace + ":" + name;
    }

}
