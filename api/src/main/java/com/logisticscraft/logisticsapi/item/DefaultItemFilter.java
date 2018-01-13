package com.logisticscraft.logisticsapi.item;

import org.bukkit.inventory.ItemStack;

public class DefaultItemFilter implements ItemFilter{

    @Override
    public boolean matchesFilter(ItemStack item) {
        return true;
    }

}
