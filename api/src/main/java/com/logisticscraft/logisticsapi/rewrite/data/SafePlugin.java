package com.logisticscraft.logisticsapi.rewrite.data;

import com.logisticscraft.logisticsapi.rewrite.persistence.Persistent;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.Optional;

@Value
@AllArgsConstructor
public class SafePlugin {

    @NonNull @Persistent private final String name;

    public SafePlugin(@NonNull Plugin plugin) {
        this(plugin.getName());
    }

    public Optional<Plugin> getPlugin() {
        return Optional.ofNullable(Bukkit.getServer().getPluginManager().getPlugin(name));
    }

}
