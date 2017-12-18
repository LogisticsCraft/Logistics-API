package com.logisticscraft.logisticsapi.excluded.item;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ItemData {

    private ItemStack item;

    public ItemData(ItemStack itemStack) {
        item = itemStack.clone();
        item.setAmount(1);
    }

    public ItemStack toItemStack() {
        return item.clone();
    }

    public boolean checkFilter(List<ItemStack> filterItems, FilteringMode filteringMode) {
        return checkFilter(filterItems, filteringMode, false);
    }

    /**
     * returns whether the given filter accepts this item object
     */
    public boolean checkFilter(List<ItemStack> filterItems, FilteringMode filteringMode, boolean ignoreEmpty) {
        if (filteringMode == FilteringMode.BLOCK_ALL) {
            return false;
        }
        if (!ignoreEmpty && filterItems.isEmpty()) {
            return true;
        }
        if (filteringMode == FilteringMode.INVERT) {
            boolean equals = false;
            for (ItemStack filterItem : filterItems) {
                equals |= filterItem.isSimilar(item);
            }
            return !equals;
        } else {
            boolean equals = false;
            for (ItemStack filterItem : filterItems) {
                if (filteringMode == FilteringMode.FILTERBY_TYPE_DAMAGE_NBT) {
                    equals |= filterItem.isSimilar(item);
                } else if (filteringMode == FilteringMode.FILTERBY_TYPE_DAMAGE) {
                    equals |= filterItem.getType() == item.getType() && filterItem.getDurability() == item.getDurability();
                } else if (filteringMode == FilteringMode.FILTERBY_TYPE_NBT) {
                    equals |= filterItem.getType() == item.getType() && filterItem.getItemMeta().equals(item.getItemMeta());
                } else if (filteringMode == FilteringMode.FILTERBY_TYPE) {
                    equals |= filterItem.getType() == item.getType();
                }
            }
            return equals;
        }
    }

}
