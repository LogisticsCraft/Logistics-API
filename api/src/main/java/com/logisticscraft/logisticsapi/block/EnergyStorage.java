package com.logisticscraft.logisticsapi.block;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface EnergyStorage extends BlockSelector {

    default long getMaxEnergyStored() {
        return AnnotationUtils.getAnnotation(this, EnergyStorageData.class).capacity();
    }

    default long getStoredEnergy() {
        return PowerManager.getInstance().getPower(getId());
    }

    default void setStoredEnergy(long energy) {
        if (energy > getMaxEnergyStored()) {
            energy = getMaxEnergyStored();
        } else if (energy < 0) {
            energy = 0;
        }
        PowerManager.getInstance().setPower(getId(), energy);
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface EnergyStorageData {

        int capacity();

    }

}
