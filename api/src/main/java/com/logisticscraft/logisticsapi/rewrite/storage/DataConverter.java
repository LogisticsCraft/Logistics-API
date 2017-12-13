package com.logisticscraft.logisticsapi.rewrite.storage;

import de.tr7zw.itemnbtapi.NBTCompound;

public interface DataConverter<K> {

    void store(K value, NBTCompound nbtContainer);
    K parse(NBTCompound nbtContainer);

}
