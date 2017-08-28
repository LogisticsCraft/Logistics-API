package com.logisticscraft.logisticsapi.energy;

import javax.annotation.Nonnull;

/**
 * @author JARvis (Пётр) PROgrammer
 */
public interface EnergyConsumer extends EnergyStorage {

    @Override
    default long getMaxInput() {
        return 0;
    }

    @Nonnull
    @Override
    default EnergySharePriority getEnergySharePriority() {
        return EnergySharePriority.NEVER;
    }
}
