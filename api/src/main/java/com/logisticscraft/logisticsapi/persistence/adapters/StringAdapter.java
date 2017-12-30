package com.logisticscraft.logisticsapi.persistence.adapters;

import com.logisticscraft.logisticsapi.persistence.PersistenceStorage;
import de.tr7zw.itemnbtapi.NBTCompound;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
public class StringAdapter implements DataAdapter<String> {

    @Override
    public void store(@NonNull final PersistenceStorage persistenceStorage, @NonNull final String value, @NonNull final NBTCompound nbtCompound) {
        nbtCompound.setString("data", value);
    }

    @Override
    public String parse(@NonNull final PersistenceStorage persistenceStorage, @NonNull final NBTCompound nbtCompound) {
        if (!nbtCompound.hasKey("data")) {
            return null;
        }
        return nbtCompound.getString("data");
    }

}
