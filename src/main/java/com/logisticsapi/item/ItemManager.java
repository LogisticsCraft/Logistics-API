package com.logisticsapi.item;

import de.tr7zw.itemnbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ItemManager {

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
    
}
