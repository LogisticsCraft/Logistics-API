package com.logisticscraft.logisticsapi.energy;

import com.logisticscraft.logisticsapi.data.LogisticBlockFace;
import com.logisticscraft.logisticsapi.util.ReflectionUtils;
import lombok.NonNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface EnergyInput extends EnergyStorage {

    default long receiveEnergy(@NonNull LogisticBlockFace blockFace, final long available, final boolean simulate) {
        synchronized (this) {
            if (!allowEnergyInput(blockFace)) {
                return 0;
            }
            long energyReceived = Math.min(getMaxEnergyStored() - getStoredEnergy(),
                    Math.min(getMaxEnergyReceive(), available));
            if (!simulate) {
                setStoredEnergy(getStoredEnergy() + energyReceived);
            }
            return energyReceived;
        }
    }

    default long getMaxEnergyReceive() {
        return ReflectionUtils.getClassAnnotation(this, EnergyInputData.class).maxReceive();
    }

    default boolean allowEnergyInput(@NonNull LogisticBlockFace blockFace) {
        return true;
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface EnergyInputData {

        int maxReceive();
    }
}
