package com.logisticscraft.logisticsapi.item;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.logisticscraft.logisticsapi.event.ItemContainerRegisterEvent;
import com.logisticscraft.logisticsapi.event.ItemContainerUnregisterEvent;
import com.logisticscraft.logisticsapi.util.console.Tracer;

import de.tr7zw.itemnbtapi.NBTItem;

public class ItemManager {

    private static Map<Location, ItemContainer> itemContainers = new HashMap<>();
    
    public static NBTItem getNBTItem(ItemStack item){
        return new NBTItem(item);
    }
    
    public static ItemStack brandItemStack(Plugin plugin, ItemStack item){
        NBTItem nbti = getNBTItem(item);
        nbti.setString("pluginid", plugin.getName());
        return nbti.getItem();
    }
    
    public static String getPluginName(ItemStack item){
        NBTItem nbti = getNBTItem(item);
        return nbti.getString("pluginid");
    }
    
    public static Boolean isPluginItem(ItemStack item){
        return getPluginName(item) != null;
    }
    
    public static Plugin getPlugin(ItemStack item){
        String name = getPluginName(item);
        if(name == null)return null;
        return Bukkit.getPluginManager().getPlugin(name);
    }
    
    public static ItemStack setOreDictionary(ItemStack item, String type){
        NBTItem nbti = getNBTItem(item);
        nbti.setString("oredictionary", type);
        return nbti.getItem();
    }
    
    public static String getOreDictionary(ItemStack item){
        NBTItem nbti = getNBTItem(item);
        return nbti.getString("oredictionary");
    }
    
    public static Boolean hasOreDictionary(ItemStack item){
        return getOreDictionary(item) != null;
    }
    
    public static void registerItemContainer(@Nonnull final Location location,
            @Nonnull final ItemContainer itemContainer) {
        if (itemContainers.putIfAbsent(location, itemContainer) == null){ 
            Tracer.msg("Item Container registered at " + location.toString());
            Bukkit.getPluginManager().callEvent(new ItemContainerRegisterEvent(location, itemContainer));
        }
        else Tracer.msg("Item Container re-registered at " + location.toString());
    }

    public static void unregisterItemContainer(@Nonnull final Location location) {
        ItemContainer container = itemContainers.get(location);
        if(container != null){
            Bukkit.getPluginManager().callEvent(new ItemContainerUnregisterEvent(location, container));
            itemContainers.remove(location);
        }else{
            Tracer.warn("Attempt to unregister unknown ItemContainer");
        }
    }

    public static boolean isContainerAt(@Nonnull final Location location) {
        return itemContainers.containsKey(location);
    }

    public static boolean isContainerRegistered(@Nonnull final ItemContainer container) {
        return itemContainers.containsValue(container);
    }

    @Nullable
    public static ItemContainer getContainerAt(@Nonnull final Location location) {
        return itemContainers.get(location);
    }

    @Nullable
    public static Location getContainerLocation(@Nonnull final ItemContainer itemContainer) {
        for (Map.Entry<Location, ItemContainer> entry : itemContainers.entrySet()) if (entry.getValue()
                == itemContainer) return entry.getKey();
        return null;
    }
    
    public static Map<Location, ItemContainer> getContainers(){
        return itemContainers;
    }
    
}
