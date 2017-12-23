package com.logisticscraft.logisticsapi.energy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.logisticscraft.logisticsapi.data.LogisticBlockFace;
import com.logisticscraft.logisticsapi.utils.ReflectionUtils;

import lombok.NonNull;

public interface EnergyOutput extends EnergyStorage {

    default long extractEnergy(@NonNull LogisticBlockFace blockFace, final long available, final boolean simulate) {
    	long energyExtracted = Math.min(getStoredEnergy(), Math.min(getMaxExtract(), available));
        if (!simulate) {
            setStoredEnergy(getStoredEnergy() - energyExtracted);
        }
        return energyExtracted;
    }

    default long getMaxExtract() {
        return ReflectionUtils.getClassAnnotation(this, EnergyOutputData.class).maxExtract();
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface EnergyOutputData {

        int maxExtract();

    }

}
