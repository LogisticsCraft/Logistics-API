package com.logisticscraft.logisticsapi.fluid;

import com.logisticscraft.logisticsapi.utils.ReflectionUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface FluidOutput extends FluidStorage {

    default long extractEnergy(final long available, final boolean simulate) {
        long amountExtracted = Math.min(getStoredFluidAmount(), Math.min(getMaxExtract(), available));
        if (!simulate) {
            setStoredFluidAmount(getStoredFluidAmount() - amountExtracted);
        }
        return amountExtracted;
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
