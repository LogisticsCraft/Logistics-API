package com.logisticscraft.logisticsapi.item;

import org.bukkit.inventory.ItemStack;

public interface ItemFilter {

    boolean matchesFilter(ItemStack item);
}
