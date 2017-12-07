package com.logisticscraft.logisticsapi.block;

public interface EnergyStorage extends LogisticBlockAccess{

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
        return getPower();
    }

    default void setEnergyStored(int energy) {
        if (energy > getMaxEnergyStored()) {
            energy = getMaxEnergyStored();
        } else if (energy < 0) {
            energy = 0;
        }
        setPower(energy);
    }

    public @interface EnergyStorageData{
        int capacity();
        int maxReceive();
        int maxExtract();
    }
}