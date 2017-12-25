package com.logisticscraft.logisticsapi.energy;

import com.logisticscraft.logisticsapi.data.LogisticBlockFace;
import com.logisticscraft.logisticsapi.utils.ReflectionUtils;
import lombok.NonNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface EnergyOutput extends EnergyStorage {

    default long extractEnergy(@NonNull LogisticBlockFace blockFace, final long available, final boolean simulate) {
        if (!allowEnergyOutput(blockFace)) return 0;
        long energyExtracted = Math.min(getStoredEnergy(), Math.min(getMaxEnergyExtract(), available));
        if (!simulate) {
            setStoredEnergy(getStoredEnergy() - energyExtracted);
        }
        return energyExtracted;
    }

    default long getMaxEnergyExtract() {
        return ReflectionUtils.getClassAnnotation(this, EnergyOutputData.class).maxExtract();
    }

    default boolean allowEnergyOutput(@NonNull LogisticBlockFace blockFace) {
        return true;
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface EnergyOutputData {

        int maxExtract();

    }

}
