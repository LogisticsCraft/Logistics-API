package com.logisticscraft.logisticsapi.persistence.adapters;

import com.logisticscraft.logisticsapi.data.holder.DataHolder;
import com.logisticscraft.logisticsapi.persistence.PersistenceStorage;

import de.tr7zw.itemnbtapi.NBTCompound;

public class DataHolderAdapter implements DataAdapter<DataHolder>{

    @Override
    public void store(PersistenceStorage persistenceStorage, DataHolder value, NBTCompound nbtCompound) {
        persistenceStorage.saveFields(value, nbtCompound);
    }

    @Override
    public DataHolder parse(PersistenceStorage persistenceStorage, Object parentObject, NBTCompound nbtCompound) {
        DataHolder holder = new DataHolder();
        persistenceStorage.loadFieldsDataObject(parentObject, holder, nbtCompound);
        return holder;
    }

}
