package com.logisticscraft.logisticsapi.rewrite.data;

import com.logisticscraft.logisticsapi.rewrite.storage.PersistantData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Delegate;
import org.bukkit.Location;

@Data
@AllArgsConstructor
public class SafeBlockLocation {

    @NonNull
    @Delegate
    @PersistantData
    private SafeWorld safeWorld;
    @PersistantData
    private int x;
    @PersistantData
    private int y;
    @PersistantData
    private int z;

    public SafeBlockLocation(Location location) {
        this(new SafeWorld(location.getWorld()), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

}
