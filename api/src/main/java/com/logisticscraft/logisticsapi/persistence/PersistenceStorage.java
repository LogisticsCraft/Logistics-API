package com.logisticscraft.logisticsapi.persistence;

import com.google.gson.Gson;
import com.logisticscraft.logisticsapi.persistence.adapters.DataAdapter;
import com.logisticscraft.logisticsapi.persistence.adapters.HashMapDataAdapter;
import com.logisticscraft.logisticsapi.persistence.adapters.StringDataAdapter;
import de.tr7zw.itemnbtapi.NBTCompound;
import lombok.NonNull;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PersistenceStorage {

    private Gson gson;
    private Objenesis objenesis;
    private Map<Class<?>, DataAdapter<?>> converters;

    public PersistenceStorage() {
        gson = new Gson();
        objenesis = new ObjenesisStd();
        converters = new HashMap<>();

        // Register default converters
        registerDataConverter(String.class, new StringDataAdapter(), false);
        registerDataConverter(HashMap.class, new HashMapDataAdapter(), false);
    }

    public <T> void registerDataConverter(@NonNull Class<T> clazz, @NonNull DataAdapter<? extends T> converter, boolean replace) {
        if (replace) {
            converters.put(clazz, converter);
        } else {
            converters.putIfAbsent(clazz, converter);
        }
    }

    @SuppressWarnings("unchecked")
    public void saveObject(@NonNull Object data, @NonNull NBTCompound nbtCompound) {
        Class<?> clazz = data.getClass();

        // Check custom converters
        if (converters.containsKey(data.getClass())) {
            ((DataAdapter<Object>) converters.get(clazz)).store(this, data, nbtCompound);
            return;
        }

        // Check serializable
        if (clazz.isAssignableFrom(Serializable.class)) {
            Arrays.stream(clazz.getDeclaredFields())
                    .filter(field -> !Modifier.isTransient(field.getModifiers()))
                    .forEach(field -> {
                        field.setAccessible(true);
                        try {
                            saveObject(field.get(data), nbtCompound.addCompound(field.getName()));
                        } catch (IllegalAccessException e) {
                            throw new IllegalStateException("Unable to serialize field " + clazz.getSimpleName()
                                    + "." + field.getName(), e);
                        }
                    });
            return;
        }

        // Fallback to Json
        nbtCompound.setString("json", gson.toJson(data));
    }

    @SuppressWarnings("unchecked")
    public <T> T loadObject(@NonNull Class<T> type, @NonNull NBTCompound nbtCompound) {
        if (converters.containsKey(type)) {
            return (T) converters.get(type).parse(this, nbtCompound);
        }

        if (type.isAssignableFrom(Serializable.class)) {
            T object = objenesis.newInstance(type);
            Arrays.stream(type.getDeclaredFields())
                    .filter(field -> !Modifier.isTransient(field.getModifiers()))
                    .forEach(field -> {
                        field.setAccessible(true);
                        try {
                            Object value = loadObject(field.getType(), nbtCompound.getCompound(field.getName()));
                            field.set(object, value);
                        } catch (IllegalAccessException e) {
                            try {
                                // Can't find any value for the field, check default value, throw exception if missing.
                                if (field.get(object) == null) {
                                    throw new IllegalStateException("Unable to deserialize field " + type.getSimpleName()
                                            + "." + field.getName(), e);
                                }
                            } catch (IllegalAccessException e1) {
                                e1.printStackTrace();
                            }
                        }
                    });
        }

        // Fallback to Json
        if (nbtCompound.hasKey("json")) {
            return gson.fromJson(nbtCompound.getString("json"), type);
        }

        return null;
    }

}
