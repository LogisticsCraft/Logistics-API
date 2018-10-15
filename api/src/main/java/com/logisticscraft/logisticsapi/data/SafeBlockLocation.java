package com.logisticscraft.logisticsapi.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.Optional;

@Data
@AllArgsConstructor
public class SafeBlockLocation {

    @NonNull
    private SafeWorld safeWorld;
    @NonNull
    private Integer x;
    @NonNull
    private Integer y;
    @NonNull
    private Integer z;

    public SafeBlockLocation(@NonNull Location location) {
        this(new SafeWorld(location.getWorld()), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public Optional<World> getWorld() {
        return safeWorld.getWorld();
    }

    public Optional<Location> getLocation() {
        return getWorld().map(world -> new Location(world, x, y, z));
    }

    public Optional<Block> getBlock() {
        return getLocation().map(Location::getBlock);
    }

    public Optional<Chunk> getChunk() {
        return getLocation().map(Location::getChunk);
    }

    public Integer getChunkX() {
        return (int) Math.ceil(x / 16);
    }

    public Integer getChunkZ() {
        return (int) Math.ceil(z / 16);
    }
}
