package com.logisticscraft.logisticsapi.block;

import de.tr7zw.itemnbtapi.NBTCompound;

public abstract class LogisticBlock implements NBTContainer, LogisticBlockAccess{

    private int power = 0;    

    @Override
    public void saveNBT(NBTCompound nbtcompound) {
        if(this instanceof EnergyStorage)
            nbtcompound.setInteger("power", power);
    }

    @Override
    public void loadNBT(NBTCompound nbtcompound) {
        if(nbtcompound.hasKey("power"))power = nbtcompound.getInteger("power");
    }

    @Override
    public int getPower() {
        return power;
    }

    @Override
    public void setPower(int power) {
        this.power = power;
    }

}
