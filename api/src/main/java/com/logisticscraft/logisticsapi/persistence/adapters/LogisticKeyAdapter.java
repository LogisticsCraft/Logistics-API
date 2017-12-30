package com.logisticscraft.logisticsapi.persistence.adapters;

import com.logisticscraft.logisticsapi.data.LogisticKey;
import com.logisticscraft.logisticsapi.persistence.PersistenceStorage;

import de.tr7zw.itemnbtapi.NBTCompound;

public class LogisticKeyAdapter implements DataAdapter<LogisticKey>{

    @Override
    public void store(PersistenceStorage persistenceStorage, LogisticKey value, NBTCompound nbtCompound) {
        nbtCompound.setString("key", value.toString());
    }

    @Override
    public LogisticKey parse(PersistenceStorage persistenceStorage, NBTCompound nbtCompound) {
        return new LogisticKey(nbtCompound.getString("key"));
    }

}
