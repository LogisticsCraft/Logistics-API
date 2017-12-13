package com.logisticscraft.logisticsapi.rewrite.persistence.adapters;

import com.logisticscraft.logisticsapi.rewrite.persistence.PersistenceStorage;
import de.tr7zw.itemnbtapi.NBTCompound;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class MapDataAdapter implements DataAdapter<Map<?, ?>> {

    @Override
    public void store(PersistenceStorage persistenceStorage, Map<?, ?> value, NBTCompound nbtCompound) {
        for (Map.Entry<?, ?> entry : value.entrySet()) {
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
    public Map parse(PersistenceStorage persistenceStorage, NBTCompound nbtCompound) {
        Map<Object, Object> map = new HashMap<>();
        for (String k : nbtCompound.getKeys()) {
            NBTCompound container = nbtCompound.getCompound(k);
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
