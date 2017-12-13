package com.logisticscraft.logisticsapi.rewrite.persistence.adapters;

import com.logisticscraft.logisticsapi.rewrite.persistence.PersistenceStorage;
import de.tr7zw.itemnbtapi.NBTCompound;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class StringDataAdapter implements DataAdapter<String> {

    @Override
    public void store(PersistenceStorage persistenceStorage, String value, NBTCompound nbtCompound) {
        nbtCompound.setString("data", value);
    }

    @Override
    public String parse(PersistenceStorage persistenceStorage, NBTCompound nbtCompound) {
        if (!nbtCompound.hasKey("data")) {
            return null;
        }
        return nbtCompound.getString("data");
    }

}
