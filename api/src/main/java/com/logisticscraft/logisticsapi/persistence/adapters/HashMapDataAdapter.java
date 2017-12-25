package com.logisticscraft.logisticsapi.persistence.adapters;

import com.logisticscraft.logisticsapi.persistence.PersistenceStorage;
import de.tr7zw.itemnbtapi.NBTCompound;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.val;

import java.util.HashMap;

@NoArgsConstructor
public class HashMapDataAdapter implements DataAdapter<HashMap<?, ?>> {

    @Override
    public void store(@NonNull final PersistenceStorage persistenceStorage, @NonNull final HashMap<?, ?> value, @NonNull final NBTCompound nbtCompound) {
        for (val entry : value.entrySet()) {
            val entryKey = entry.getKey();
            val entryValue = entry.getValue();
            val container = nbtCompound.addCompound("" + entryKey.hashCode());
            val keyData = container.addCompound("key");
            val data = container.addCompound("data");
            nbtCompound.setString("keyclass", entryKey.getClass().getName());
            nbtCompound.setString("dataclass", entryValue.getClass().getName());
            persistenceStorage.saveObject(entryKey, keyData);
            persistenceStorage.saveObject(entryValue, data);
        }
    }

    @Override
    public HashMap<Object, Object> parse(@NonNull PersistenceStorage persistenceStorage, @NonNull NBTCompound nbtCompound) {
        HashMap<Object, Object> map = new HashMap<>();
        for (val key : nbtCompound.getKeys()) {
            val container = nbtCompound.getCompound(key);
            val keyData = container.getCompound("key");
            val data = container.getCompound("data");
            try {
                map.put(persistenceStorage.loadObject(Class.forName(container.getString("keyclass")), keyData),
                        persistenceStorage.loadObject(Class.forName(container.getString("dataclass")), data));
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
        return map;
    }

}
