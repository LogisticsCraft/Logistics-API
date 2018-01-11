package com.logisticscraft.logisticsapi.persistence.adapters;

import org.bukkit.inventory.ItemStack;

import com.logisticscraft.logisticsapi.persistence.PersistenceStorage;

import de.tr7zw.itemnbtapi.NBTCompound;
import de.tr7zw.itemnbtapi.NBTItem;

public class ItemStackAdapter implements DataAdapter<ItemStack> {

    @Override
    public void store(PersistenceStorage persistenceStorage, ItemStack value, NBTCompound nbtCompound) {
        nbtCompound.mergeCompound(NBTItem.convertItemtoNBT(value));
    }

    @Override
    public ItemStack parse(PersistenceStorage persistenceStorage, Object parentObject, NBTCompound nbtCompound) {
        return NBTItem.convertNBTtoItem(nbtCompound);
    }

}
