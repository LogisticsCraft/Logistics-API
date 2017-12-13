package com.logisticscraft.logisticsapi.rewrite.storage;

import de.tr7zw.itemnbtapi.NBTCompound;

public interface NBTContainer{

    void saveNBT(NBTCompound nbtcompound);
    void loadNBT(NBTCompound nbtcompound);
    
}
