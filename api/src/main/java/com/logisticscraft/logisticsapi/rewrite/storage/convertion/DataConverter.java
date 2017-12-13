package com.logisticscraft.logisticsapi.rewrite.storage.convertion;

import de.tr7zw.itemnbtapi.NBTCompound;

public interface DataConverter<K> {

    void store(final K value, final NBTCompound nbtCompound);
    K parse(final NBTCompound nbtCompound);

}
