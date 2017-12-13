package com.logisticscraft.logisticsapi.rewrite.storage.convertion;

import de.tr7zw.itemnbtapi.NBTCompound;

public class StringDataConverter implements DataConverter<String>{

    @Override
    public void store(String value, NBTCompound nbtCompound) {
        nbtCompound.setString("data", value);
    }

    @Override
    public String parse(NBTCompound nbtCompound) {
        if(!nbtCompound.hasKey("data"))return null;
        return nbtCompound.getString("data");
    }

}
