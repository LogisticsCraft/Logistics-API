package com.logisticscraft.logisticsapi.data;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.Serializable;
import java.util.Optional;

@Value
@AllArgsConstructor
public class SafePlugin implements Serializable {

    @NonNull
    private final String name;

    public SafePlugin(@NonNull final Plugin plugin) {
        this(plugin.getName());
    }

    public Optional<Plugin> getPlugin() {
        return Optional.ofNullable(Bukkit.getServer().getPluginManager().getPlugin(name));
    }

}
