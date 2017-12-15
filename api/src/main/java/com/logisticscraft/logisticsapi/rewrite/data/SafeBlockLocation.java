package com.logisticscraft.logisticsapi.rewrite.data;

import com.logisticscraft.logisticsapi.rewrite.persistence.Persistent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Delegate;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.Optional;

@Data
@AllArgsConstructor
public class SafeBlockLocation {

    @NonNull @Delegate @Persistent private SafeWorld safeWorld;
    @Persistent private int x;
    @Persistent private int y;
    @Persistent private int z;

    public SafeBlockLocation(Location location) {
        this(new SafeWorld(location.getWorld()), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public Optional<Location> getLocation() {
        return getWorld().map(world -> new Location(world, x, y, z));
    }

    public Optional<Block> getBlock() {
        return getLocation().map(Location::getBlock);
    }

}
