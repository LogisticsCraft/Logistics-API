package com.logisticscraft.logisticsapi.fluid;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Optional;

import com.logisticscraft.logisticsapi.data.LogisticBlockFace;
import com.logisticscraft.logisticsapi.utils.ReflectionUtils;

import lombok.NonNull;

public interface FluidInput extends FluidStorage {

    default long receiveFluid(@NonNull LogisticBlockFace blockFace, @NonNull LogisticFluid fluid, final long available, final boolean simulate) {
        if(!allowFluidInput(blockFace, Optional.of(fluid)))return 0;
        if(getStoredFluidType().isPresent() && !getStoredFluidType().get().equals(fluid))return 0;
    	long amountReceived = Math.min(getMaxFluidStored() - getStoredFluidAmount(), Math.min(getMaxFluidReceive(), available));
        if (!simulate) {
        	setStoredFluid(fluid, getStoredFluidAmount() + amountReceived);
        }
        return amountReceived;
    }

    default long getMaxFluidReceive() {
        return ReflectionUtils.getClassAnnotation(this, FluidInputData.class).maxReceive();
    }
    
    default boolean allowFluidInput(@NonNull LogisticBlockFace blockFace, Optional<LogisticFluid> fluid){
    	return true;
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface FluidInputData {

        int maxReceive();

    }

}
