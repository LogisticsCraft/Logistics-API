package com.logisticscraft.logisticsapi.excluded.energy.storage;

import lombok.NonNull;

public interface EnergyProducer extends EnergyStorage {

    @Override
    default long getMaxOutput() {
        return 0;
    }

    @NonNull
    @Override
    default EnergySharePriority getEnergySharePriority() {
        return EnergySharePriority.ALWAYS;
    }
}
