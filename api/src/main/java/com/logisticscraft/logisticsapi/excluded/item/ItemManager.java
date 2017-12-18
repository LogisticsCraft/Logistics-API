package com.logisticscraft.logisticsapi.excluded.item;

import com.logisticscraft.logisticsapi.event.ItemContainerRegisterEvent;
import com.logisticscraft.logisticsapi.event.ItemContainerUnregisterEvent;
import com.logisticscraft.logisticsapi.utils.Tracer;
import de.tr7zw.itemnbtapi.NBTItem;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@UtilityClass
public class ItemManager {
    ///////////////////////////////////////////////////////////////////////////
    // Containers storage
    ///////////////////////////////////////////////////////////////////////////

    @Getter
    private Map<Location, ItemContainer> itemContainers = new ConcurrentHashMap<>();

    public boolean isContainerAt(@NonNull final Location location) {
        return itemContainers.containsKey(location);
    }

    public boolean isContainerRegistered(@NonNull final ItemContainer container) {
        return itemContainers.containsValue(container);
    }

    public ItemContainer getContainerAt(@NonNull final Location location) {
        return itemContainers.get(location);
    }

    public Location getContainerLocation(@NonNull final ItemContainer itemContainer) {
        for (val entry : itemContainers.entrySet()) if (entry.getValue() == itemContainer) return entry.getKey();
        return null;
    }

    public void registerItemContainer(@NonNull final Location location, @NonNull final ItemContainer itemContainer) {
        if (itemContainers.putIfAbsent(location, itemContainer) == null) {
            Tracer.info("ItemContainer registered at " + location.toString());
            Bukkit.getPluginManager().callEvent(new ItemContainerRegisterEvent(location, itemContainer));
        } else Tracer.warn("Trying to register ItemContainer at occupied location: " + location.toString());
    }

    public void unregisterItemContainer(@NonNull final Location location) {
        val container = itemContainers.get(location);
        if (container != null) {
            Bukkit.getPluginManager().callEvent(new ItemContainerUnregisterEvent(location, container));
            itemContainers.remove(location);
        } else Tracer.warn("Attempt to unregister unknown ItemContainer");
    }

    ///////////////////////////////////////////////////////////////////////////
    // NBTItems
    ///////////////////////////////////////////////////////////////////////////

    public static NBTItem getNBTItem(ItemStack item) {
        return new NBTItem(item);
    }

    public ItemStack brandItemStack(Plugin plugin, ItemStack item) {
        val nbtItem = getNBTItem(item);
        nbtItem.setString("pluginid", plugin.getName());
        return nbtItem.getItem();
    }

    public String getPluginName(ItemStack item) {
        val nbtItem = getNBTItem(item);
        return nbtItem.getString("pluginid");
    }

    public boolean isPluginItem(ItemStack item) {
        return getPluginName(item) != null;
    }

    public Plugin getPlugin(ItemStack item) {
        val name = getPluginName(item);
        return name == null ? null : Bukkit.getPluginManager().getPlugin(name);
    }

    public ItemStack setOreDictionary(ItemStack item, String type) {
        val nbtItem = getNBTItem(item);
        nbtItem.setString("oredictionary", type);
        return nbtItem.getItem();
    }

    public String getOreDictionary(ItemStack item) {
        val nbtItem = getNBTItem(item);
        return nbtItem.getString("oredictionary");
    }

    public boolean hasOreDictionary(ItemStack item) {
        return getOreDictionary(item) != null;
    }

}
