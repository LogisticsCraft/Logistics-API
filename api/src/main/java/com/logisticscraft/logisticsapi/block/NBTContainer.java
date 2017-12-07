package com.logisticscraft.logisticsapi.block;

import de.tr7zw.itemnbtapi.NBTCompound;

public interface NBTContainer extends RuntimeCache{

    public void saveNBT(NBTCompound nbtcompound);
    
    public void loadNBT(NBTCompound nbtcompound);
    
}
