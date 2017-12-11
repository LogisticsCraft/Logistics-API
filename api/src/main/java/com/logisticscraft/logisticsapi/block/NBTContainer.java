package com.logisticscraft.logisticsapi.block;

import de.tr7zw.itemnbtapi.NBTCompound;

public interface NBTContainer{

    void saveNBT(NBTCompound nbtcompound);
    void loadNBT(NBTCompound nbtcompound);
    
}
