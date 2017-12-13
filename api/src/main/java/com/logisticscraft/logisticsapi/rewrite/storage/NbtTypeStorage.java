package com.logisticscraft.logisticsapi.rewrite.storage;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.logisticscraft.logisticsapi.rewrite.storage.convertion.DataConverter;
import com.logisticscraft.logisticsapi.rewrite.storage.convertion.StringDataConverter;

import de.tr7zw.itemnbtapi.NBTCompound;
import lombok.NonNull;

public class NbtTypeStorage {

    private Gson gson = new Gson();
    private Map<Class<?>, DataConverter<?>> converters = new HashMap<>();

    public NbtTypeStorage(){
        registerDataConverter(String.class, new StringDataConverter(), false);
    }

    public <T> void registerDataConverter(@NonNull Class<? extends T> clazz, @NonNull DataConverter<T> converter, boolean replace) {
        if(replace){
            converters.put(clazz, converter);
        }else{
            converters.putIfAbsent(clazz, converter);
        }
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
