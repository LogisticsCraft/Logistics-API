package com.logisticscraft.logisticsapi.rewrite.storage.convertion;

import java.util.HashMap;

import com.google.common.collect.Maps;
import com.logisticscraft.logisticsapi.LogisticsApiPlugin;

import de.tr7zw.itemnbtapi.NBTCompound;

public class HashMapDataConverter implements DataConverter<HashMap>{

    @Override
    public void store(HashMap value, NBTCompound nbtCompound) {
        for(Object key : value.keySet()){
            NBTCompound container = nbtCompound.addCompound(""+key.hashCode());
            NBTCompound keydata = container.addCompound("key");
            NBTCompound data = container.addCompound("data");
            nbtCompound.setString("keyclass", key.getClass().getName());
            nbtCompound.setString("dataclass", value.get(key).getClass().getName());
            LogisticsApiPlugin.getInstance().getNbtTypeStorage().saveFieldData(key, keydata);
            LogisticsApiPlugin.getInstance().getNbtTypeStorage().saveFieldData(value.get(key), data);
        }
    }

    @Override
    public HashMap parse(NBTCompound nbtCompound) {
        HashMap<Object, Object> map = Maps.newHashMap();
        for(String k : nbtCompound.getKeys()){
            NBTCompound container = nbtCompound.getCompound(k);
            NBTCompound keydata = container.getCompound("key");
            NBTCompound data = container.getCompound("data");
            try{
                map.put(LogisticsApiPlugin.getInstance().getNbtTypeStorage().loadFieldData(Class.forName(container.getString("keyclass")), keydata), 
                        LogisticsApiPlugin.getInstance().getNbtTypeStorage().loadFieldData(Class.forName(container.getString("dataclass")), data));
            }catch(ClassNotFoundException ex){
                ex.printStackTrace();
            }
        }
        return map;
    }


}
