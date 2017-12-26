package com.logisticscraft.logisticsapi.persistence.adapters;

import com.logisticscraft.logisticsapi.persistence.PersistenceStorage;
import de.tr7zw.itemnbtapi.NBTCompound;

public interface DataAdapter<K> {

    void store(final PersistenceStorage persistenceStorage, final K value, final NBTCompound nbtCompound);

    K parse(final PersistenceStorage persistenceStorage, final NBTCompound nbtCompound);

}
