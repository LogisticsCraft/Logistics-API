package com.logisticscraft.logisticsapi.rewrite.energy;

import com.logisticscraft.logisticsapi.rewrite.utils.AnnotationUtils;
import com.logisticscraft.logisticsapi.rewrite.Metadatable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface EnergyStorage extends Metadatable {

    String STORED_ENERGY_META_TAG = "storedEnergy";

    default long getMaxEnergyStored() {
        return AnnotationUtils.getAnnotation(this, EnergyStorageData.class).capacity();
    }

    default long getStoredEnergy() {
        return getMetadata(STORED_ENERGY_META_TAG, Long.class).orElse(0L);
    }

    default void setStoredEnergy(final long energy) {
        long newEnergy;
        if (energy > getMaxEnergyStored()) {
            newEnergy = getMaxEnergyStored();
        } else if (energy < 0) {
            newEnergy = 0;
        } else {
            newEnergy = energy;
        }
        setMetadata(STORED_ENERGY_META_TAG, newEnergy);
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface EnergyStorageData {

        int capacity();

    }

}
