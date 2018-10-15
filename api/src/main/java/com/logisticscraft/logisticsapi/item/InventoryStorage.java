package com.logisticscraft.logisticsapi.item;

import com.logisticscraft.logisticsapi.data.LogisticBlockFace;
import com.logisticscraft.logisticsapi.data.LogisticKey;
import com.logisticscraft.logisticsapi.util.ReflectionUtils;
import lombok.NonNull;
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

import static com.logisticscraft.logisticsapi.util.ItemUtils.changeItemAmount;

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
        Optional<Inventory> optionalInventory = getPersistentData().get(STORED_INVENTORY_META_KEY, Inventory.class);
        if (optionalInventory.isPresent()) {
            return optionalInventory.get();
        }
        Inventory inventory = Bukkit.createInventory(getInventoryHolder(), getRowAmount() * 9, getInventoryName());
        getPersistentData().set(STORED_INVENTORY_META_KEY, inventory);
        return inventory;
    }

    @Override
    default ItemStack extractItem(LogisticBlockFace extractionSide, int maxExtractAmount, ItemFilter filter, boolean simulate) {
        Inventory cachedInventory = getStoredInventory();
        ItemStack takenItem = null;
        for (int i = 0; i < cachedInventory.getSize(); i++) {
            if (cachedInventory.getItem(i) != null && filter.matchesFilter(cachedInventory.getItem(i))) {
                int amountBefore = takenItem != null ? takenItem.getAmount() : 0;
                if (takenItem == null && allowItemExtraction(extractionSide, cachedInventory.getItem(i))) {
                    takenItem = cachedInventory.getItem(i).clone();
                    takenItem.setAmount(Math.min(maxExtractAmount, takenItem.getAmount()));
                } else if (takenItem.isSimilar(cachedInventory.getItem(i))) {
                    takenItem.setAmount(Math.min(maxExtractAmount, amountBefore + cachedInventory.getItem(i).getAmount()));
                }
                ItemStack item = cachedInventory.getItem(i);
                if (!simulate) {
                    cachedInventory.setItem(i, changeItemAmount(item, -(takenItem.getAmount() - amountBefore)));
                }
            }
        }
        return takenItem;
    }

    @Override
    default ItemStack insertItem(LogisticBlockFace insertSide, ItemStack insertedItem, boolean simulate) {
        if (simulate) {
            int space = howMuchSpaceForItemAsync(insertSide, insertedItem);
            if (space == 0) {
                return insertedItem;
            }
            if (space >= insertedItem.getAmount()) {
                return null;
            }
            ItemStack cloneItem = insertedItem.clone();
            cloneItem.setAmount(cloneItem.getAmount() - space);
            return cloneItem;
        }
        if (!allowItemInsertion(insertSide, insertedItem)) {
            return insertedItem;
        }
        Inventory cachedInv = getStoredInventory();
        Collection<ItemStack> overflow = cachedInv.addItem(insertedItem).values();
        if (overflow.isEmpty()) {
            return null;
        } else {
            return overflow.toArray(new ItemStack[0])[0];
        }
    }

    @Override
    default int howMuchSpaceForItemAsync(@NonNull LogisticBlockFace insertDirection, @NonNull ItemStack insertion) {
        if (!allowItemInsertion(insertDirection, insertion)) {
            return 0;
        }
        Inventory cachedInv = getStoredInventory();
        int freeSpace = 0;
        for (int i = 0; i < cachedInv.getSize(); i++) {
            ItemStack is = cachedInv.getItem(i);
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
