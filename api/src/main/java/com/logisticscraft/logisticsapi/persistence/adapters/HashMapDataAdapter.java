package com.logisticscraft.logisticsapi.persistence.adapters;

import com.logisticscraft.logisticsapi.persistence.PersistenceStorage;
import de.tr7zw.itemnbtapi.NBTCompound;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@NoArgsConstructor
public class HashMapDataAdapter implements DataAdapter<HashMap<?, ?>> {

    @Override
    public void store(final PersistenceStorage persistenceStorage, final HashMap<?, ?> value, final NBTCompound nbtCompound) {
        for (HashMap.Entry<?, ?> entry : value.entrySet()) {
            Object entryKey = entry.getKey();
            Object entryValue = entry.getValue();
            NBTCompound container = nbtCompound.addCompound("" + entryKey.hashCode());
            NBTCompound keyData = container.addCompound("key");
            NBTCompound data = container.addCompound("data");
            nbtCompound.setString("keyclass", entryKey.getClass().getName());
            nbtCompound.setString("dataclass", entryValue.getClass().getName());
            persistenceStorage.saveFieldData(entryKey, keyData);
            persistenceStorage.saveFieldData(entryValue, data);
        }
    }

    @Override
    public HashMap<Object, Object> parse(PersistenceStorage persistenceStorage, NBTCompound nbtCompound) {
        HashMap<Object, Object> map = new HashMap<>();
        for (String key : nbtCompound.getKeys()) {
            NBTCompound container = nbtCompound.getCompound(key);
            NBTCompound keyData = container.getCompound("key");
            NBTCompound data = container.getCompound("data");
            try {
                map.put(persistenceStorage.loadFieldData(Class.forName(container.getString("keyclass")), keyData),
                        persistenceStorage.loadFieldData(Class.forName(container.getString("dataclass")), data));
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
        return map;
    }

}
