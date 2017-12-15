package com.logisticscraft.logisticsapi.energy.storage;

import lombok.NonNull;

public interface EnergyConsumer extends EnergyStorage {

    @Override
    default long getMaxInput() {
        return 0;
    }

    @Override
    @NonNull default EnergySharePriority getEnergySharePriority() {
        return EnergySharePriority.NEVER;
    }
}
