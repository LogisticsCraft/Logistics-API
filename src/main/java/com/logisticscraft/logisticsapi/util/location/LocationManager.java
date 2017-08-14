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
    public static Location deserializeBlockLocation(@Nonnull String locationSerialized) {
        try {
            int splitterIndex = locationSerialized.indexOf('|');
            Long locationCoords = Long.parseLong(locationSerialized.substring(splitterIndex + 1));

            return new Location(Bukkit.getWorld(locationSerialized.substring(0, splitterIndex)),
                    locationCoords >> 36, locationCoords << 28 >> 56, locationCoords << 36 >> 36);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nonnull
    public static String serializeBlockLocation(@Nonnull Location location) {
        return location.getWorld().getName() + "|" +
                (((long) location.getBlockX() & 0xFFFFFFF) << 36
                        | (((long) location.getBlockY() & 0xFF) << 28)
                        | ((long) location.getBlockZ() & 0xFFFFFFF));
    }
}
