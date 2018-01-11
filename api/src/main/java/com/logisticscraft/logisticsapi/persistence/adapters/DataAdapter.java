package com.logisticscraft.logisticsapi.persistence.adapters;

import com.logisticscraft.logisticsapi.persistence.PersistenceStorage;
import de.tr7zw.itemnbtapi.NBTCompound;
import lombok.NonNull;

public interface DataAdapter<K> {

    void store(@NonNull final PersistenceStorage persistenceStorage, final K value, @NonNull final NBTCompound nbtCompound);

    K parse(@NonNull final PersistenceStorage persistenceStorage, @NonNull Object parentObject, @NonNull final NBTCompound nbtCompound);

}
