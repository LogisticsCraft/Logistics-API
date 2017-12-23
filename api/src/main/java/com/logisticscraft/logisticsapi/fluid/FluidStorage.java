package com.logisticscraft.logisticsapi.fluid;

import com.logisticscraft.logisticsapi.data.LogisticDataHolder;
import com.logisticscraft.logisticsapi.data.LogisticKey;
import com.logisticscraft.logisticsapi.utils.ReflectionUtils;
import lombok.NonNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Optional;

public interface FluidStorage extends LogisticDataHolder {

    LogisticKey STORED_FLUID_TYPE_META_KEY = new LogisticKey("LogisticsAPI", "storedFluidType");
    LogisticKey STORED_FLUID_META_KEY = new LogisticKey("LogisticsAPI", "storedFluid");

    default long getMaxFluidStored() {
        return ReflectionUtils.getClassAnnotation(this, EnergyStorageData.class).capacity();
    }

    default Optional<LogisticFluid> getStoredFluidType() {
        return getLogisticData(STORED_FLUID_TYPE_META_KEY, LogisticFluid.class);
    }

    default long getStoredFluid() {
        return getLogisticData(STORED_FLUID_META_KEY, Long.class).orElse(0L);
    }

    default void setStoredFluid(final long fluid) {
        if (!getStoredFluidType().isPresent()) {
            throw new IllegalStateException("Tried to set the amount of fluid in an empty storage!");
        }

        long newFluid;
        if (fluid > getMaxFluidStored()) {
            newFluid = getMaxFluidStored();
        } else if (fluid < 0) {
            newFluid = 0;
        } else {
            newFluid = fluid;
        }

        if (newFluid == 0) {
            removeLogisticData(STORED_FLUID_TYPE_META_KEY);
            removeLogisticData(STORED_FLUID_META_KEY);
            return;
        }

        setLogisticData(STORED_FLUID_META_KEY, newFluid);
    }

    default void setStoredFluid(@NonNull final LogisticFluid fluidType, final long fluid) {
        Optional<LogisticFluid> currentFluid = getStoredFluidType();
        if (currentFluid.isPresent() && !currentFluid.get().equals(fluidType)) {
            throw new IllegalStateException("Tried to change the fluid type of a non empty fluid storage!");
        }

        long newFluid;
        if (fluid > getMaxFluidStored()) {
            newFluid = getMaxFluidStored();
        } else if (fluid < 0) {
            newFluid = 0;
        } else {
            newFluid = fluid;
        }

        if (newFluid == 0) {
            removeLogisticData(STORED_FLUID_TYPE_META_KEY);
            removeLogisticData(STORED_FLUID_META_KEY);
            return;
        }

        setLogisticData(STORED_FLUID_TYPE_META_KEY, fluidType);
        setLogisticData(STORED_FLUID_META_KEY, newFluid);
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface EnergyStorageData {

        int capacity();

    }

}
