package com.logisticscraft.logisticsapi.persistence.adapters;

import com.logisticscraft.logisticsapi.persistence.PersistenceStorage;

import de.tr7zw.changeme.nbtapi.NBTCompound;

public class IntegerAdapter implements DataAdapter<Integer> {

    @Override
    public void store(PersistenceStorage persistenceStorage, Integer value, NBTCompound nbtCompound) {
        nbtCompound.setInteger("int", value);
    }

    @Override
    public Integer parse(PersistenceStorage persistenceStorage, Object parentObject, NBTCompound nbtCompound) {
        if (!nbtCompound.hasKey("int")) {
            return null;
        }
        return nbtCompound.getInteger("int");
    }
}
