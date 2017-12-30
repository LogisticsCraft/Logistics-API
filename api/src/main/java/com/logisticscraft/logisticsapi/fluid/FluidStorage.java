package com.logisticscraft.logisticsapi.fluid;

import com.logisticscraft.logisticsapi.data.LogisticKey;
import com.logisticscraft.logisticsapi.data.PersistentLogisticDataHolder;
import com.logisticscraft.logisticsapi.utils.ReflectionUtils;
import lombok.NonNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Optional;

public interface FluidStorage extends PersistentLogisticDataHolder {

    LogisticKey STORED_FLUID_TYPE_META_KEY = new LogisticKey("LogisticsAPI", "storedFluidType");
    LogisticKey STORED_FLUID_AMOUNT_META_KEY = new LogisticKey("LogisticsAPI", "storedFluidAmount");

    default long getMaxFluidStored() {
        return ReflectionUtils.getClassAnnotation(this, EnergyStorageData.class).capacity();
    }

    default Optional<LogisticFluid> getStoredFluidType() {
        return getLogisticData(STORED_FLUID_TYPE_META_KEY, LogisticFluid.class);
    }

    default long getStoredFluidAmount() {
        return getLogisticData(STORED_FLUID_AMOUNT_META_KEY, Long.class).orElse(0L);
    }

    default void setStoredFluidAmount(final long fluid) {
        if (!getStoredFluidType().isPresent()) {
            throw new IllegalStateException("Tried to set the amount of fluid in an empty storage!");
        }

        long newAmount;
        if (fluid > getMaxFluidStored()) {
            newAmount = getMaxFluidStored();
        } else if (fluid < 0) {
            newAmount = 0;
        } else {
            newAmount = fluid;
        }

        if (newAmount == 0) {
            removeLogisticData(STORED_FLUID_TYPE_META_KEY);
            removeLogisticData(STORED_FLUID_AMOUNT_META_KEY);
            return;
        }

        setLogisticData(STORED_FLUID_AMOUNT_META_KEY, newAmount);
    }

    default void setStoredFluid(@NonNull final LogisticFluid fluidType, final long amount) {
        long newAmount;
        if (amount > getMaxFluidStored()) {
            newAmount = getMaxFluidStored();
        } else if (amount < 0) {
            newAmount = 0;
        } else {
            newAmount = amount;
        }

        if (newAmount == 0) {
            removeLogisticData(STORED_FLUID_TYPE_META_KEY);
            removeLogisticData(STORED_FLUID_AMOUNT_META_KEY);
            return;
        }

        setLogisticData(STORED_FLUID_TYPE_META_KEY, fluidType);
        setLogisticData(STORED_FLUID_AMOUNT_META_KEY, newAmount);
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface EnergyStorageData {

        int capacity();

    }

}
