package com.logisticscraft.logisticsapi.energy;

import com.logisticscraft.logisticsapi.data.LogisticBlockFace;
import com.logisticscraft.logisticsapi.utils.ReflectionUtils;

import lombok.NonNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface EnergyInput extends EnergyStorage {

    default long receiveEnergy(@NonNull LogisticBlockFace blockFace, final long available, final boolean simulate) {
        long energyReceived = Math.min(getMaxEnergyStored() - getStoredEnergy(), Math.min(getMaxReceive(), available));
        if (!simulate) {
            setStoredEnergy(getStoredEnergy() + energyReceived);
        }
        return energyReceived;
    }

    default long getMaxReceive() {
        return ReflectionUtils.getClassAnnotation(this, EnergyInputData.class).maxReceive();
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface EnergyInputData {

        int maxReceive();

    }

}
