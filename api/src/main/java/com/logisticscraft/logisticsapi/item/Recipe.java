package com.logisticscraft.logisticsapi.item;

import org.bukkit.NamespacedKey;

public interface Recipe {

    void register();
    
    NamespacedKey getNamespacedKey();
    
}
