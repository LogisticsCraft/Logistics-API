package com.logisticscraft.logisticsapi.block;

import de.tr7zw.itemnbtapi.NBTCompound;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface EnergyStorage extends PowerHolder, NBTContainer {

    default long getMaxEnergyStored() {
        return AnnotationUtils.getAnnotation(this, EnergyStorageData.class).capacity();
    }

    default long getStoredEnergy() {
        return getPower();
    }

    default void setStoredEnergy(long energy) {
        if (energy > getMaxEnergyStored()) {
            energy = getMaxEnergyStored();
        } else if (energy < 0) {
            energy = 0;
        }
        setPower(energy);
    }

    @Override
    default void saveNBT(NBTCompound nbtCompound) {
        nbtCompound.setLong("power", getStoredEnergy());
    }

    @Override
    default void loadNBT(NBTCompound nbtcompound) {
        setStoredEnergy(nbtcompound.getLong("power"));
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface EnergyStorageData {

        int capacity();

    }

}
