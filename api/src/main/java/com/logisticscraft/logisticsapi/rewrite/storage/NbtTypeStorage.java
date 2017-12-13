package com.logisticscraft.logisticsapi.rewrite.storage;

import com.google.gson.Gson;
import com.logisticscraft.logisticsapi.rewrite.storage.convertion.DataConverter;
import de.tr7zw.itemnbtapi.NBTCompound;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class NbtTypeStorage {

    private Gson gson = new Gson();
    private Map<Class<?>, DataConverter<?>> converters = new HashMap<>();

    public <T> void registerDataConverter(@NonNull Class<? extends T> clazz, @NonNull DataConverter<T> converter) {
        converters.put(clazz, converter);
    }

    @SuppressWarnings("unchecked")
    public void saveFieldData(@NonNull Object data, @NonNull NBTCompound nbtCompound) {
        // Check custom converters
        if (converters.containsKey(data.getClass())) {
            ((DataConverter<Object>) converters.get(data.getClass())).store(data, nbtCompound);
            return;
        }

        // Check annotated fields
        boolean hasAnnotation = false;
        for (Field field : data.getClass().getDeclaredFields()) {
            if (field.getAnnotation(PersistantData.class) == null) {
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
            return (T) converters.get(type).parse(nbtCompound);
        }

        // TODO: Implement annotated fields loading!

        // Fallback to Json
        if (nbtCompound.hasKey("json")) {
            return gson.fromJson(nbtCompound.getString("json"), type);
        }
        return null;
    }

}
