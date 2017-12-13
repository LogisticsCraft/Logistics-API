package com.logisticscraft.logisticsapi.rewrite.persistence;

import com.google.gson.Gson;
import com.logisticscraft.logisticsapi.rewrite.persistence.adapters.DataAdapter;
import com.logisticscraft.logisticsapi.rewrite.persistence.adapters.HashMapDataAdapter;
import com.logisticscraft.logisticsapi.rewrite.persistence.adapters.StringDataAdapter;
import de.tr7zw.itemnbtapi.NBTCompound;
import lombok.NonNull;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class PersistenceStorage {

    private Gson gson;
    private Map<Class<?>, DataAdapter<?>> converters;

    public PersistenceStorage() {
        gson = new Gson();
        converters = new HashMap<>();

        // Register default converters
        registerDataConverter(String.class, new StringDataAdapter(), false);
        registerDataConverter(HashMap.class, new HashMapDataAdapter(), false);
    }

    public <T> void registerDataConverter(@NonNull Class<? extends T> clazz, @NonNull DataAdapter<T> converter, boolean replace) {
        if (replace) {
            converters.put(clazz, converter);
        } else {
            converters.putIfAbsent(clazz, converter);
        }
    }

    @SuppressWarnings("unchecked")
    public void saveFieldData(@NonNull Object data, @NonNull NBTCompound nbtCompound) {
        // Check custom converters
        if (converters.containsKey(data.getClass())) {
            ((DataAdapter<Object>) converters.get(data.getClass())).store(this, data, nbtCompound);
            return;
        }

        // Check annotated fields
        boolean hasAnnotation = false;
        for (Field field : data.getClass().getDeclaredFields()) {
            if (field.getAnnotation(Persistent.class) == null) {
                continue;
            }

            hasAnnotation = true;
            field.setAccessible(true);
            try {
                saveFieldData(field.get(data), nbtCompound.addCompound(field.getName()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (hasAnnotation) {
            return;
        }

        // Fallback to Json
        nbtCompound.setString("json", gson.toJson(data));
    }

    @SuppressWarnings("unchecked")
    public <T> T loadFieldData(@NonNull Class<T> type, @NonNull NBTCompound nbtCompound) {
        if (converters.containsKey(type)) {
            return (T) converters.get(type).parse(this, nbtCompound);
        }

        // TODO: Implement annotated fields loading!

        // Fallback to Json
        if (nbtCompound.hasKey("json")) {
            return gson.fromJson(nbtCompound.getString("json"), type);
        }
        return null;
    }

}
