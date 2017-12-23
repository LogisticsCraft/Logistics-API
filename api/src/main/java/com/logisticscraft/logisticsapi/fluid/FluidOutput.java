package com.logisticscraft.logisticsapi.fluid;

import com.logisticscraft.logisticsapi.utils.ReflectionUtils;

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
        return ReflectionUtils.getClassAnnotation(this, FluidOutputData.class).maxExtract();
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface FluidOutputData {

        int maxExtract();

    }

}
