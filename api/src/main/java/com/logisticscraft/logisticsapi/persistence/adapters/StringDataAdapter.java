package com.logisticscraft.logisticsapi.persistence.adapters;

import com.logisticscraft.logisticsapi.persistence.PersistenceStorage;
import de.tr7zw.itemnbtapi.NBTCompound;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class StringDataAdapter implements DataAdapter<String> {

    @Override
    public void store(final PersistenceStorage persistenceStorage, final String value, final NBTCompound nbtCompound) {
        nbtCompound.setString("data", value);
    }

    @Override
    public String parse(final PersistenceStorage persistenceStorage, final NBTCompound nbtCompound) {
        if (!nbtCompound.hasKey("data")) {
            return null;
        }
        return nbtCompound.getString("data");
    }

}
