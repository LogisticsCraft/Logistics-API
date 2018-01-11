package com.logisticscraft.logisticsapi.persistence;

import com.google.gson.Gson;
import com.logisticscraft.logisticsapi.data.LogisticKey;
import com.logisticscraft.logisticsapi.data.SafeBlockLocation;
import com.logisticscraft.logisticsapi.data.holder.DataHolder;
import com.logisticscraft.logisticsapi.persistence.adapters.*;
import com.logisticscraft.logisticsapi.utils.ReflectionUtils;
import com.logisticscraft.logisticsapi.utils.Tracer;

import de.tr7zw.itemnbtapi.NBTCompound;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PersistenceStorage {

    private Gson gson;
    private Map<Class<?>, DataAdapter<?>> converters;
    private Map<Class<?>, DataAdapter<?>> interfaceConverters;

    public PersistenceStorage() {
        gson = new Gson();
        converters = new HashMap<>();

        // Register default converters
        registerDataConverter(String.class, new StringAdapter(), false);
        registerDataConverter(HashMap.class, new HashMapAdapter(), false);
        registerDataConverter(SafeBlockLocation.class, new SafeBlockLocationAdapter(), false);
        registerDataConverter(LogisticKey.class, new LogisticKeyAdapter(), false);
        registerDataConverter(DataHolder.class, new DataHolderAdapter(), false);
        
        interfaceConverters = new HashMap<>();
        registerInterfaceConverter(Inventory.class, new InventoryAdapter(), false);
        registerInterfaceConverter(ItemStack.class, new ItemStackAdapter(), false);
    }

    public <T> void registerInterfaceConverter(@NonNull Class<T> clazz, @NonNull DataAdapter<? extends T> converter, boolean replace) {
        if (replace) {
            interfaceConverters.put(clazz, converter);
        } else {
            interfaceConverters.putIfAbsent(clazz, converter);
        }
    }
    
    public <T> void registerDataConverter(@NonNull Class<T> clazz, @NonNull DataAdapter<? extends T> converter, boolean replace) {
        if (replace) {
            converters.put(clazz, converter);
        } else {
            converters.putIfAbsent(clazz, converter);
        }
    }

    public void saveFields(@NonNull Object object, @NonNull NBTCompound nbtCompound) {
        ReflectionUtils.getFieldsRecursively(object.getClass(), Object.class).stream()
                .filter(field -> field.getAnnotation(Persistent.class) != null)
                .forEach(field -> {
                    field.setAccessible(true);
                    try {
                        saveObject(field.get(object), nbtCompound.addCompound(field.getName()));
                    } catch (IllegalAccessException e) {
                        throw new IllegalStateException("Unable to save field " + object.getClass().getSimpleName()
                                + "." + field.getName(), e);
                    }
                });
    }

    public void loadFields(@NonNull Object object, @NonNull NBTCompound nbtCompound) {
        loadFieldsDataObject(object, object, nbtCompound);
    }
    
    public void loadFieldsDataObject(@NonNull Object parent, @NonNull Object object, @NonNull NBTCompound nbtCompound) {
        ReflectionUtils.getFieldsRecursively(object.getClass(), Object.class).stream()
                .filter(field -> field.getAnnotation(Persistent.class) != null)
                .forEach(field -> {
                    field.setAccessible(true);
                    if (nbtCompound.hasKey(field.getName())) {
                        try {
                            field.set(object, loadObject(parent, field.getType(), nbtCompound.getCompound(field.getName())));
                        } catch (IllegalAccessException e) {
                            throw new IllegalStateException("Unable to load field " + object.getClass().getSimpleName()
                                    + "." + field.getName(), e);
                        }
                    }
                });
    }

    @SuppressWarnings("unchecked")
    public Class<?> saveObject(@NonNull Object data, @NonNull NBTCompound nbtCompound) {
        Class<?> clazz = data.getClass();

        // Check custom converters
        if (converters.containsKey(data.getClass())) {
            ((DataAdapter<Object>) converters.get(clazz)).store(this, data, nbtCompound);
            return clazz;
        }
        
        for(Entry<Class<?>, DataAdapter<?>> ent : interfaceConverters.entrySet()){
            if(ent.getKey().isInstance(data)){
                ((DataAdapter<Object>) ent.getValue()).store(this, data, nbtCompound);
                return ent.getKey();
            }
        }

        // Fallback to Json
        Tracer.warn("Did not find a Wrapper for " + data.getClass().getName() + "! Falling back to Gson!");
        nbtCompound.setString("json", gson.toJson(data));
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> T loadObject(@NonNull Object parent, @NonNull Class<T> type, @NonNull NBTCompound nbtCompound) {
        if (converters.containsKey(type)) {
            return (T) converters.get(type).parse(this, parent, nbtCompound);
        }
        
        for(Entry<Class<?>, DataAdapter<?>> ent : interfaceConverters.entrySet()){
            if(ent.getKey().isAssignableFrom(type)){
                return (T) ent.getValue().parse(this, parent, nbtCompound);
            }
        }

        // Fallback to Json
        if (nbtCompound.hasKey("json")) {
            return gson.fromJson(nbtCompound.getString("json"), type);
        }

        return null;
    }

}
