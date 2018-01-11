package com.logisticscraft.logisticsapi.persistence.adapters;

import com.logisticscraft.logisticsapi.persistence.PersistenceStorage;

import de.tr7zw.itemnbtapi.NBTCompound;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class StringAdapter implements DataAdapter<String> {

    @Override
    public void store(PersistenceStorage persistenceStorage, String value, NBTCompound nbtCompound) {
        nbtCompound.setString("data", value);
    }

    @Override
    public String parse(PersistenceStorage persistenceStorage, Object parentObject, NBTCompound nbtCompound) {
        if (!nbtCompound.hasKey("data")) {
            return null;
        }
        return nbtCompound.getString("data");
    }

}
