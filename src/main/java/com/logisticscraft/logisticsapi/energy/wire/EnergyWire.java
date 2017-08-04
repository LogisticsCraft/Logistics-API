package com.logisticscraft.logisticsapi.energy.wire;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import com.logisticscraft.logisticsapi.energy.EnergyManager;
import com.logisticscraft.logisticsapi.util.console.Tracer;

import javax.annotation.Nonnull;

/**
 * @author JARvis (Пётр) PROgrammer
 * TODO
 */
public interface EnergyWire {
    long getWireEnergy();

    void setWireEnergy(long energy);

    long getWireMaxEnergy();

    long getWireMaxInput();

    long getWireMaxOutput();

    default float getWireEnergyRate() {
        return (float) getWireEnergy() / (float) getWireMaxEnergy();
    }

    default long getWireSpaceLeft() {
        return getWireMaxEnergy() - getWireEnergy();
    }

    /**
     * Force-Takes some amount of Energy from the given updateWireNear
     *
     * @param energy Energy to be taken from the updateWireNear
     * @return amount of Energy given with updateWireNear (less than or equal to energy asked)
     */
    default long takeWireEnergy(long energy) {
        Tracer.msg("Taking Energy: " + energy);
        Tracer.msg("Current: " + getWireEnergy());
        if (getWireEnergy() <= energy) {
            setWireEnergy(getWireEnergy() - energy);
            return energy;
        } else {
            long result = getWireEnergy();
            setWireEnergy(0);
            return result;
        }
    }

    /**
     * Force-Puts some amount of Energy into the given updateWireNear
     *
     * @param energy Energy to be put into the updateWireNear
     * @return amount of Energy not put into the updateWireNear (because of it not having space)
     */
    default long putWireEnergy(long energy) {
        long sum = getWireEnergy() + energy;
        if (getWireMaxEnergy() >= sum) {
            setWireEnergy(sum);
            return 0;
        } else {
            long result = energy - getWireMaxEnergy();
            setWireEnergy(getWireMaxEnergy());
            return result;
        }
    }

    Location getWireLocation();

    default void updateWireNear() {
        for (Location location : getSideLocations()) {
            if (WireManager.isWireAt(location)) {
                EnergyWire wire = WireManager.getWireAt(location);

                assert (wire != null);

                if (wire.getWireEnergyRate() > getWireEnergyRate()) {
                    long energyToTake = Math.min(Math.min(wire.getWireEnergy(),
                            Math.min(getWireMaxInput(), wire.getWireMaxOutput())), getWireSpaceLeft());
                    wire.takeWireEnergy(energyToTake);
                    putWireEnergy(energyToTake);
                }
            }
        }
    }

    Location[] getSideLocations();

    void setSideLocations(@Nonnull Location[] sideLocations);

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
