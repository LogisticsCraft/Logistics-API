package com.logisticscraft.logisticsapi.item;

import org.bukkit.NamespacedKey;

public interface Recipe {

    void register();
    org.bukkit.inventory.Recipe getBukkitRecipe();
    
    NamespacedKey getNamespacedKey();
    
}
