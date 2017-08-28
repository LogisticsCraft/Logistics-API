package com.logisticscraft.logisticsapi.registry;

import com.logisticscraft.logisticsapi.annotation.ApiComponent;
import com.logisticscraft.logisticsapi.util.console.Tracer;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * @author JARvis (Пётр) PROgrammer
 */
public class LogistcsRegistry {
    private static Map<LogisticsNamespaceKey, LogisticObjectData> objects = new HashMap<>();

    public static Map<LogisticsNamespaceKey, LogisticObjectData> getObjects() {
        return objects;
    }

    @ApiComponent
    public static boolean register(@Nonnull LogisticObjectData objectData, boolean force) {
        if (force) {
            objects.put(objectData.getNamespaceKey(), objectData);
            Tracer.msg("LogisticObject was FORCE-registered by NamespaceKey \""
                    + objectData.getNamespaceKey() + "\"");
            return true;
        } else if (objects.putIfAbsent(objectData.getNamespaceKey(), objectData) == null) {
            Tracer.msg("LogisticObject was registered by NamespaceKey \""
                    + objectData.getNamespaceKey() + "\"");
            return true;
        } else {
            Tracer.warn("An attempt to register LogisticObject by NamespaceKey \""
                    + objectData.getNamespaceKey() + "\"which is already in use");
            return false;
        }
    }

    @ApiComponent
    public static LogisticObjectData getObject(@Nonnull JavaPlugin plugin, @Nonnull String name) {
        return getObject(new LogisticsNamespaceKey(plugin, name));
    }

    @ApiComponent
    public static LogisticObjectData getObject(@Nonnull LogisticsNamespaceKey namespaceKey) {
        return objects.get(namespaceKey);
    }
}
