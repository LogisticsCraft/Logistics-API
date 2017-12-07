package com.logisticscraft.logisticsapi.energy.wire;

import com.logisticscraft.logisticsapi.registry.LogisticObject;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import com.logisticscraft.logisticsapi.energy.storage.EnergyManager;
import com.logisticscraft.logisticsapi.util.logger.Tracer;

public interface EnergyWire extends LogisticObject {
    ///////////////////////////////////////////////////////////////////////////
    // General Energy Amount
    ///////////////////////////////////////////////////////////////////////////
    long getEnergy();

    void setEnergy(long energy);

    long getMaxEnergy();

    long getMaxInput();

    long getMaxOutput();

    default float getEnergyRate() {
        return (float) getEnergy() / (float) getMaxEnergy();
    }

    default long getEnergySpaceLeft() {
        return getMaxEnergy() - getEnergy();
    }

    /**
     * Force-Takes some amount of Energy from the given updateWireNear
     *
     * @param energy Energy to be taken from the updateWireNear
     * @return amount of Energy given with updateWireNear (less than or equal to energy asked)
     */
    default long takeEnergy(long energy) {
        Tracer.info("Taking Energy: " + energy);
        Tracer.info("Current: " + getEnergy());
        if (getEnergy() <= energy) {
            setEnergy(getEnergy() - energy);
            return energy;
        } else {
            long result = getEnergy();
            setEnergy(0);
            return result;
        }
    }

    /**
     * Force-Puts some amount of Energy into the given updateWireNear
     *
     * @param energy Energy to be put into the updateWireNear
     * @return amount of Energy not put into the updateWireNear (because of it not having space)
     */
    default long putEnergy(long energy) {
        long sum = getEnergy() + energy;
        if (getMaxEnergy() >= sum) {
            setEnergy(sum);
            return 0;
        } else {
            long result = energy - getMaxEnergy();
            setEnergy(getMaxEnergy());
            return result;
        }
    }

    /**
     * Returns the current Location of Wire-Instance
     * Is highly recommended to use BlockLocation
     * @return location of the Wire
     */
    Location getWireLocation();

    default void updateWireNear() {
        for (Location location : getSideLocations()) {
            if (WireManager.isWireAt(location)) {
                EnergyWire wire = WireManager.getWireAt(location);

                assert (wire != null);

                if (wire.getEnergyRate() > getEnergyRate()) {
                    long energyToTake = Math.min(Math.min(wire.getEnergy(),
                            Math.min(getMaxInput(), wire.getMaxOutput())), getEnergySpaceLeft());
                    wire.takeEnergy(energyToTake);
                    putEnergy(energyToTake);
                }
            }
        }
    }

    Location[] getSideLocations();

    void setSideLocations(@NonNull Location[] sideLocations);

    default void setupSideLocations(Location location) {
        Location[] sideLocations = new Location[6];

        int i = 0;
        for (Vector vector : EnergyManager.SIDE_VECTORS.values()) {
            sideLocations[i] = location.clone().add(vector);
            i++;
        }

        setSideLocations(sideLocations);
    }

    default void setupSideLocations() {
        setupSideLocations(getWireLocation());
    }
}
