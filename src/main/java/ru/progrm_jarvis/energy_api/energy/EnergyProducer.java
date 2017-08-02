package ru.progrm_jarvis.energy_api.energy;

import javax.annotation.Nonnull;

/**
 * @author JARvis (Пётр) PROgrammer
 */
public interface EnergyProducer extends EnergyStorage {

    @Override
    default long getMaxOutput() {
        return 0;
    }

    @Nonnull
    @Override
    default EnergySharePriority getSharePriority() {
        return EnergySharePriority.ALWAYS;
    }
}
