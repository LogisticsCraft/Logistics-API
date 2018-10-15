package com.logisticscraft.logisticsapi.persistence.adapters;

import com.logisticscraft.logisticsapi.item.InventoryStorage;
import com.logisticscraft.logisticsapi.persistence.PersistenceStorage;
import de.tr7zw.itemnbtapi.NBTCompound;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryAdapter implements DataAdapter<Inventory> {

    @Override
    public void store(PersistenceStorage persistenceStorage, Inventory inventory, NBTCompound nbtCompound) {
        nbtCompound.setString("type", inventory.getType().name());
        nbtCompound.setString("name", inventory.getTitle());
        nbtCompound.setInteger("size", inventory.getSize());
        int slot = 0;
        for (ItemStack item : inventory.getContents()) {
            if (item != null)
                persistenceStorage.saveObject(item, nbtCompound.addCompound("item" + slot));
            slot++;
        }
    }

    @Override
    public Inventory parse(PersistenceStorage persistenceStorage, Object parentObject, NBTCompound nbtCompound) {
        if (parentObject instanceof InventoryStorage) {
            Inventory inv = null;
            InventoryType type = InventoryType.valueOf(nbtCompound.getString("type"));
            if (type == InventoryType.CHEST) {
                inv = Bukkit.createInventory(((InventoryStorage) parentObject).getInventoryHolder(),
                        nbtCompound.getInteger("size"), nbtCompound.getString("name"));
            } else {
                inv = Bukkit.createInventory(((InventoryStorage) parentObject).getInventoryHolder(), type,
                        nbtCompound.getString("name"));
            }
            int slot = 0;
            while (slot < nbtCompound.getInteger("size")) {
                if (nbtCompound.hasKey("item" + slot)) {
                    inv.setItem(slot, persistenceStorage.loadObject(parentObject, ItemStack.class,
                            nbtCompound.getCompound("item" + slot)));
                }
                slot++;
            }
            return inv;
        }
        return null;
    }
}
