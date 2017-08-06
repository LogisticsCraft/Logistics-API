package com.logisticscraft.logisticsapi.util.location;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author JARvis (Пётр) PROgrammer
 */
public class LocationManager {
    @Nullable
    public static Location deserializeLocation(@Nonnull String locationSerialized) {
        try {
            String[] locationData = locationSerialized.split("\\|");
            Long locationCoords = Long.parseLong(locationData[1]);

            return new Location(Bukkit.getWorld(locationData[0]),
                    locationCoords >> 38, (locationCoords << 26 >> 52) + 1, locationCoords << 38 >> 38);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nonnull
    public static String serializeLocation(@Nonnull Location location) {
        return location.getWorld().getName() + "|" +
                (long) (location.getX() * (long) Math.pow(2, 38)
                        + location.getY() * (long) Math.pow(2, 26)
                        + location.getZ()
                );
    }
}
