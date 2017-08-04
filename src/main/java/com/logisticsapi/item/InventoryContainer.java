package com.logisticsapi.item;

import java.util.Collection;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface InventoryContainer extends ItemContainer{

    public Inventory getInventory();

    public static ItemStack decreaseAmountWithOne(ItemStack item) {
        ItemStack copy = item.clone();
        if (item.getAmount() > 1) {
            copy.setAmount(item.getAmount() - 1);
        } else {
            copy = null;
        }
        return copy;
    }

    @Override
    public default ItemStack extractItem(BlockFace extractDirection) {
        Inventory cachedInv = getInventory();
        for (int i = 0; i < cachedInv.getSize(); i++) {
            if (cachedInv.getItem(i) != null) {
                ItemStack id = cachedInv.getItem(i);
                cachedInv.setItem(i, decreaseAmountWithOne(cachedInv.getItem(i)));
                return id;
            }
        }
        return null;
    }

    @Override
    public default boolean insertItem(BlockFace insertDirection, ItemStack insertion) {
        Collection<ItemStack> overflow = getInventory().addItem(insertion).values();
        return overflow.isEmpty();
    }

    @Override
    public default boolean isSpaceForItemAsync(BlockFace insertDirection, ItemStack insertion) {
        Inventory cachedInv = getInventory();
        for (int i = 0; i < cachedInv.getSize(); i++) {
            ItemStack is = cachedInv.getItem(i);
            if (is == null || is.getType() == Material.AIR) {
                return true;
            }
            if (is.isSimilar(insertion) && is.getAmount() < is.getMaxStackSize()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public default boolean isInterfaceable(BlockFace face) {
        return true;
    }

}
