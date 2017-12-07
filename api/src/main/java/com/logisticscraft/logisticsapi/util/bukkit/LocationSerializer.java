package com.logisticscraft.logisticsapi.util.bukkit;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;

@UtilityClass
public class LocationSerializer {

    public static Location deserializeBlockLocation(@NonNull String locationSerialized) {
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

    @NonNull
    public static String serializeBlockLocation(@NonNull Location location) {
        return location.getWorld().getName() + "|" +
                (((long) location.getBlockX() & 0xFFFFFFF) << 36
                        | (((long) location.getBlockY() & 0xFF) << 28)
                        | ((long) location.getBlockZ() & 0xFFFFFFF));
    }
}
