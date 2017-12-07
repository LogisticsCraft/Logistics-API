package com.logisticscraft.logisticsapi.registry;

import com.logisticscraft.logisticsapi.annotation.ApiComponent;
import com.logisticscraft.logisticsapi.util.logger.Tracer;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class LogisticsRegistry {

    private Map<LogisticsNamespaceKey, LogisticObjectData> objects = new HashMap<>();

    @ApiComponent
    public boolean register(@NonNull LogisticObjectData objectData, boolean force) {
        if (force) {
            objects.put(objectData.getNamespaceKey(), objectData);
            Tracer.info("LogisticObject was FORCE-registered by NamespaceKey \""
                    + objectData.getNamespaceKey() + "\"");
            return true;
        } else if (objects.putIfAbsent(objectData.getNamespaceKey(), objectData) == null) {
            Tracer.info("LogisticObject was registered by NamespaceKey \""
                    + objectData.getNamespaceKey() + "\"");
            return true;
        } else {
            Tracer.warn("An attempt to register LogisticObject by NamespaceKey \""
                    + objectData.getNamespaceKey() + "\"which is already in use");
            return false;
        }
    }

    @ApiComponent
    public LogisticObjectData getObject(@NonNull JavaPlugin plugin, @NonNull String name) {
        return getObject(new LogisticsNamespaceKey(plugin, name));
    }

    @ApiComponent
    public LogisticObjectData getObject(@NonNull LogisticsNamespaceKey namespaceKey) {
        return objects.get(namespaceKey);
    }

}
