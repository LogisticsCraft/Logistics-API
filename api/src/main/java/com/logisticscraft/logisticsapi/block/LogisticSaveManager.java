package com.logisticscraft.logisticsapi.block;

import java.util.HashMap;

import com.google.gson.Gson;

import de.tr7zw.itemnbtapi.NBTCompound;

public class LogisticSaveManager {

    private Gson gson = new Gson();
    private HashMap<Class<?>, DataConverter<?>> converters = new HashMap<>();
    
    public <T> void registerDataConverter(Class<? extends T> clazz, DataConverter<T> converter){
        converters.put(clazz, converter);
    }
    
    public void saveData(Object data, NBTCompound nbtCompound){
        if(converters.containsKey(data.getClass())){
            ((DataConverter<Object>)converters.get(data.getClass())).store(data, nbtCompound);
            return;
        }
        //TODO: Having stuff like int long String here or as DataConverter?
        
        //Fallback: Using Gson. Let's see how Gson likes Bukkit Worlds Kappa
        nbtCompound.setString("json", gson.toJson(data));
    }
    
    public <T> T loadData(Class<T> type, NBTCompound nbtCompound){
        if(converters.containsKey(type)){
            return (T) converters.get(type).parse(nbtCompound);
        }
        
        //TODO: Having stuff like int long String here or as DataConverter?
        
        //Gson fallback
        if(nbtCompound.hasKey("json")){
            return gson.fromJson(nbtCompound.getString("json"), type);
        }
        return null;
    }
    
}
