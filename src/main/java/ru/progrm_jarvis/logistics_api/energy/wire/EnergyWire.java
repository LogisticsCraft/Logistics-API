package ru.progrm_jarvis.logistics_api.energy.wire;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import ru.progrm_jarvis.logistics_api.energy.EnergyManager;
import ru.progrm_jarvis.logistics_api.util.console.Tracer;

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
            Tracer.msg("var1");
            setWireEnergy(getWireEnergy() - energy);
            return energy;
        } else {
            Tracer.msg("var2");
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
        Tracer.msg("Putting Energy: " + energy);
        Tracer.msg("Current: " + getWireEnergy());
        long sum = getWireEnergy() + energy;
        Tracer.msg("Sum: " + sum);
        if (getWireMaxEnergy() >= sum) {
            Tracer.msg("var1");
            setWireEnergy(sum);
            return 0;
        } else {
            Tracer.msg("var2");
            long result = energy - getWireMaxEnergy();
            setWireEnergy(getWireMaxEnergy());
            return result;
        }
    }

    Location getWireLocation();

    default void updateWireNear() {
        Tracer.msg("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
        for (Location location : getSideLocations()) {
            if (WireManager.isWireAt(location)) {
                EnergyWire wire = WireManager.getWireAt(location);

                assert (wire != null);

                if (wire.getWireEnergyRate() > getWireEnergyRate()) {
                    Tracer.msg("Doing smth.");
                    long energyToTake = Math.min(Math.min(wire.getWireEnergy(),
                            Math.min(getWireMaxInput(), wire.getWireMaxOutput())), getWireSpaceLeft());

                    Tracer.msg("ToTake: " + energyToTake);

                    Tracer.msg("Taking E.1: " + wire.getWireEnergy());
                    wire.takeWireEnergy(energyToTake);
                    Tracer.msg("Taking E.2: " + wire.getWireEnergy());

                    Tracer.msg("Putting E.1: " + getWireEnergy());
                    putWireEnergy(energyToTake);
                    Tracer.msg("Putting E.2: " + getWireEnergy());
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
