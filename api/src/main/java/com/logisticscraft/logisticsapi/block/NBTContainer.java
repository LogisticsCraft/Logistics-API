package com.logisticscraft.logisticsapi.block;

import de.tr7zw.itemnbtapi.NBTCompound;

public interface NBTContainer{

    public void saveNBT(NBTCompound nbtcompound);
    
    public void loadNBT(NBTCompound nbtcompound);
    
}
