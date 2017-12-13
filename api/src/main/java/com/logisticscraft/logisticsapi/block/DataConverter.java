package com.logisticscraft.logisticsapi.block;

import de.tr7zw.itemnbtapi.NBTCompound;

public interface DataConverter<K> {

    void store(K value, NBTCompound nbtContainer);
    
    K parse(NBTCompound nbtContainer);
    
}
