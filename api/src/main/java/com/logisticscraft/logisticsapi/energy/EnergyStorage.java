package com.logisticscraft.logisticsapi.energy;

import com.logisticscraft.logisticsapi.data.LogisticKey;
import com.logisticscraft.logisticsapi.data.holder.PersistentDataHolder;
import com.logisticscraft.logisticsapi.utils.ReflectionUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface EnergyStorage extends PersistentDataHolder {

    LogisticKey STORED_ENERGY_META_KEY = new LogisticKey("LogisticsAPI", "storedEnergy");

    default long getMaxEnergyStored() {
        return ReflectionUtils.getClassAnnotation(this, EnergyStorageData.class).capacity();
    }

    default long getStoredEnergy() {
        return getPersistentData().get(STORED_ENERGY_META_KEY, Long.class).orElse(0L);
    }

    default void setStoredEnergy(final long energy) {
        final long newEnergy;
        if (energy > getMaxEnergyStored())
            newEnergy = getMaxEnergyStored();
        else if (energy < 0)
            newEnergy = 0;
        else
            newEnergy = energy;

        if (newEnergy == 0) {
            getPersistentData().remove(STORED_ENERGY_META_KEY);
            return;
        }

        getPersistentData().set(STORED_ENERGY_META_KEY, newEnergy);
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface EnergyStorageData {

        int capacity();

    }

}
