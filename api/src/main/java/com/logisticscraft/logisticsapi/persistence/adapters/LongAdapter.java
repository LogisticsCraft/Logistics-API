package com.logisticscraft.logisticsapi.persistence.adapters;

import com.logisticscraft.logisticsapi.persistence.PersistenceStorage;

import de.tr7zw.changeme.nbtapi.NBTCompound;

public class LongAdapter implements DataAdapter<Long> {

    @Override
    public void store(PersistenceStorage persistenceStorage, Long value, NBTCompound nbtCompound) {
        nbtCompound.setLong("long", value);
    }

    @Override
    public Long parse(PersistenceStorage persistenceStorage, Object parentObject, NBTCompound nbtCompound) {
        if (!nbtCompound.hasKey("long")) {
            return null;
        }
        return nbtCompound.getLong("long");
    }
}
