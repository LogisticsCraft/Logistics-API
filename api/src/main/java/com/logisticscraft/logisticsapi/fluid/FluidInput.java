package com.logisticscraft.logisticsapi.fluid;

import com.logisticscraft.logisticsapi.utils.AnnotationUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface FluidInput extends FluidStorage {

    default long receiveEnergy(final long maxEnergy, final boolean simulate) {
        long energyReceived = Math.min(getMaxFluidStored() - getStoredFluid(), Math.min(getMaxReceive(), maxEnergy));
        if (!simulate) {
            setStoredFluid(getStoredFluid() + energyReceived);
        }
        return energyReceived;
    }

    default long getMaxReceive() {
        return AnnotationUtils.getClassAnnotation(this, FluidInputData.class).maxReceive();
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface FluidInputData {

        int maxReceive();

    }

}
