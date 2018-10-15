package com.logisticscraft.logisticsapi.fluid;

import com.google.common.collect.Maps;
import com.logisticscraft.logisticsapi.data.LogisticBlockFace;
import com.logisticscraft.logisticsapi.utils.ReflectionUtils;
import lombok.NonNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map.Entry;
import java.util.Optional;

public interface FluidOutput extends FluidStorage {

    default Optional<Entry<LogisticFluid, Long>> extractFluid(@NonNull LogisticBlockFace blockFace, final long limit,
                                                              final boolean simulate) {
        if (!getStoredFluidType().isPresent()) {
            return Optional.empty();
        }
        long amountExtracted = Math.min(getStoredFluidAmount(), Math.min(getMaxFluidExtract(), limit));
        if (!simulate) {
            setStoredFluidAmount(getStoredFluidAmount() - amountExtracted);
        }
        return Optional.of(Maps.immutableEntry(getStoredFluidType().get(), amountExtracted));
    }

    default long getMaxFluidExtract() {
        return ReflectionUtils.getClassAnnotation(this, FluidOutputData.class).maxExtract();
    }

    default boolean allowFluidOutput(@NonNull LogisticBlockFace blockFace) {
        return true;
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface FluidOutputData {

        int maxExtract();
    }
}
