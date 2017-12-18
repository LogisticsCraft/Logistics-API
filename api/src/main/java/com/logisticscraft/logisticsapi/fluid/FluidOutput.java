package com.logisticscraft.logisticsapi.fluid;

import com.logisticscraft.logisticsapi.utils.AnnotationUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface FluidOutput extends FluidStorage {

    default long extractEnergy(final long maxExtract, final boolean simulate) {
        long energyExtracted = Math.min(getStoredFluid(), Math.min(getMaxExtract(), maxExtract));
        if (!simulate) {
            setStoredFluid(getStoredFluid() - energyExtracted);
        }
        return energyExtracted;
    }

    default long getMaxExtract() {
        return AnnotationUtils.getClassAnnotation(this, EnergyOutputData.class).maxExtract();
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface EnergyOutputData {

        int maxExtract();

    }

}
