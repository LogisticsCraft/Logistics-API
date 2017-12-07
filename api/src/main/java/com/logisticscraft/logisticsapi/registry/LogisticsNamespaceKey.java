package com.logisticscraft.logisticsapi.registry;

import com.logisticscraft.logisticsapi.annotation.ApiComponent;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

@AllArgsConstructor(onConstructor = @__({@ApiComponent}))
@EqualsAndHashCode
public class LogisticsNamespaceKey {

    @Getter(onMethod = @__({@ApiComponent}))
    private Plugin plugin;
    @Getter(onMethod = @__({@ApiComponent}))
    private String name;

    @Override
    public String toString() {
        return plugin.getName() + ":" + name;
    }

}
