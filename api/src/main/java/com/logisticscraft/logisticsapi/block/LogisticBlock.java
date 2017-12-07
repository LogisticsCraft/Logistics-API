package com.logisticscraft.logisticsapi.block;

import de.tr7zw.itemnbtapi.NBTCompound;

public abstract class LogisticBlock implements NBTContainer{

    private final NBTCompound cache = new NBTCache();
    
    @Override
    public NBTCompound getRuntimeData() {
        return cache;
    }

    @Override
    public void saveNBT(NBTCompound nbtcompound) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void loadNBT(NBTCompound nbtcompound) {
        // TODO Auto-generated method stub
        
    }
    
    
    
}
