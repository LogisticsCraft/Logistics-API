package com.logisticscraft.logisticsapi.fluid;

import com.logisticscraft.logisticsapi.utils.ReflectionUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface FluidInput extends FluidStorage {

    default long receiveEnergy(final long available, final boolean simulate) {
        long amountReceived = Math.min(getMaxFluidStored() - getStoredFluidAmount(), Math.min(getMaxReceive(), available));
        if (!simulate) {
            setStoredFluidAmount(getStoredFluidAmount() + amountReceived);
        }
        return amountReceived;
    }

    default long getMaxReceive() {
        return ReflectionUtils.getClassAnnotation(this, FluidInputData.class).maxReceive();
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface FluidInputData {

        int maxReceive();

    }

}
