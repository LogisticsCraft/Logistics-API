package com.logisticscraft.logisticsapi.item;

import com.logisticscraft.logisticsapi.data.LogisticBlockFace;
import com.logisticscraft.logisticsapi.data.LogisticKey;
import com.logisticscraft.logisticsapi.utils.ItemUtils;
import com.logisticscraft.logisticsapi.utils.ReflectionUtils;
import lombok.NonNull;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;
import java.util.Optional;

public interface InventoryStorage extends ItemStorage {

    LogisticKey STORED_INVENTORY_META_KEY = new LogisticKey("LogisticsAPI", "storedInventory");

    default int getRowAmount() {
        return ReflectionUtils.getClassAnnotation(this, InventoryData.class).rows();
    }

    default String getInventoryName() {
        return ReflectionUtils.getClassAnnotation(this, InventoryData.class).name();
    }

    InventoryHolder getInventoryHolder();

    default Inventory getStoredInventory() {
        val optionalInventory = getLogisticData(STORED_INVENTORY_META_KEY, Inventory.class);
        if (optionalInventory.isPresent()) return optionalInventory.get();
        val inventory = Bukkit.createInventory(getInventoryHolder(), getRowAmount() * 9, getInventoryName());
        setLogisticData(STORED_INVENTORY_META_KEY, inventory);
        return inventory;
    }

    @Override
    default ItemStack extractItem(@NonNull LogisticBlockFace extractionSide, int maxExtractAmount, boolean simulate) {
        val cachedInv = getStoredInventory();
        ItemStack takenIs = null;
        for (int i = 0; i < cachedInv.getSize(); i++) {
            if (cachedInv.getItem(i) != null) {
                int amountBefore = takenIs != null ? takenIs.getAmount() : 0;
                if (takenIs == null && allowItemExtraction(extractionSide, cachedInv.getItem(i))) {
                    takenIs = cachedInv.getItem(i).clone();
                    takenIs.setAmount(Math.min(maxExtractAmount, takenIs.getAmount()));
                } else if (takenIs.isSimilar(cachedInv.getItem(i))) {
                    takenIs.setAmount(Math.min(maxExtractAmount, amountBefore + cachedInv.getItem(i).getAmount()));
                }
                val invItem = cachedInv.getItem(i);
                if (!simulate)
                    cachedInv.setItem(i, ItemUtils.changeItemAmount(invItem, -(takenIs.getAmount() - amountBefore)));
            }
        }
        return takenIs;
    }

    @Override
    default ItemStack insertItem(@NonNull LogisticBlockFace insertSide, @NonNull ItemStack insertedItem, boolean simulate) {
        if (simulate) {
            int space = howMuchSpaceForItemAsync(insertSide, insertedItem);
            if (space == 0) return insertedItem;
            if (space >= insertedItem.getAmount()) return null;
            val cloneItem = insertedItem.clone();
            cloneItem.setAmount(cloneItem.getAmount() - space);
            return cloneItem;
        }
        if (!allowItemInsertion(insertSide, insertedItem)) return insertedItem;
        val cachedInv = getStoredInventory();
        val overflow = cachedInv.addItem(insertedItem).values();
        if (overflow.isEmpty()) {
            return null;
        } else {
            return overflow.toArray(new ItemStack[0])[0];
        }
    }

    @Override
    default int howMuchSpaceForItemAsync(@NonNull LogisticBlockFace insertDirection, @NonNull ItemStack insertion) {
        if (!allowItemInsertion(insertDirection, insertion)) return 0;
        val cachedInv = getStoredInventory();
        int freeSpace = 0;
        for (int i = 0; i < cachedInv.getSize(); i++) {
            val is = cachedInv.getItem(i);
            if (is == null || is.getType() == Material.AIR) {
                freeSpace += insertion.getMaxStackSize();
            } else if (is.isSimilar(insertion) && is.getAmount() < is.getMaxStackSize()) {
                freeSpace += is.getMaxStackSize() - is.getAmount();
            }
        }
        return freeSpace;
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface InventoryData {
        int rows();

        String name();
    }

}
