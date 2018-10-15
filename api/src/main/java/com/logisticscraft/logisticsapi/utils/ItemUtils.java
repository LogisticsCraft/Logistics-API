package com.logisticscraft.logisticsapi.utils;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;

@UtilityClass
public class ItemUtils {

    public static ItemStack changeItemAmount(@NonNull ItemStack item, int amountDelta) {
        ItemStack copy = item.clone();
        if (item.getAmount() + amountDelta > 0) {
            copy.setAmount(Math.min(item.getMaxStackSize(), item.getAmount() + amountDelta));
        } else {
            copy = null;
        }
        return copy;
    }
}
