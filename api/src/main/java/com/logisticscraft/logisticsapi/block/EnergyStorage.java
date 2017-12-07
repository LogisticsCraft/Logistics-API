package com.logisticscraft.logisticsapi.block;

import de.tr7zw.itemnbtapi.NBTCompound;

public interface EnergyStorage extends NBTContainer{

    default int getMaxEnergyStored(){
        if(getClass().getAnnotation(EnergyStorageData.class) == null){
            new Exception("The class '" + getClass().getName() + "' does not have the @EnergyStorageData Annotation!").printStackTrace();
            return 0;
        }
        return getClass().getAnnotation(EnergyStorageData.class).capacity();
    }

    default int getMaxReceive(){
        if(getClass().getAnnotation(EnergyStorageData.class) == null){
            new Exception("The class '" + getClass().getName() + "' does not have the @EnergyStorageData Annotation!").printStackTrace();
            return 0;
        }
        return getClass().getAnnotation(EnergyStorageData.class).maxReceive();
    }

    default int getMaxExtract(){
        if(getClass().getAnnotation(EnergyStorageData.class) == null){
            new Exception("The class '" + getClass().getName() + "' does not have the @EnergyStorageData Annotation!").printStackTrace();
            return 0;
        }
        return getClass().getAnnotation(EnergyStorageData.class).maxExtract();
    }


    default int getEnergyStored(){
        if(getRuntimeData().hasKey("energy"))return getRuntimeData().getInteger("energy");
        return 0;
    }

    default void setEnergyStored(int energy) {
        if (energy > getMaxEnergyStored()) {
            energy = getMaxEnergyStored();
        } else if (energy < 0) {
            energy = 0;
        }
        getRuntimeData().setInteger("energy", energy);
    }

    @Override
    default void saveNBT(NBTCompound nbtcompound) {
        nbtcompound.setInteger("energy", getEnergyStored());
    }

    @Override
    default void loadNBT(NBTCompound nbtcompound) {
        if(nbtcompound.hasKey("energy"))
            setEnergyStored(nbtcompound.getInteger("energy"));
    }

    public @interface EnergyStorageData{
        int capacity();
        int maxReceive();
        int maxExtract();
    }
}