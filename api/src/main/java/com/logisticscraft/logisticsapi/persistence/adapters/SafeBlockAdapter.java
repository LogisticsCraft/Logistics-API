package com.logisticscraft.logisticsapi.persistence.adapters;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import com.logisticscraft.logisticsapi.data.SafeBlockLocation;
import com.logisticscraft.logisticsapi.persistence.PersistenceStorage;

import de.tr7zw.itemnbtapi.NBTCompound;

public class SafeBlockAdapter implements DataAdapter<SafeBlockLocation> {

    @Override
    public void store(PersistenceStorage persistenceStorage, SafeBlockLocation value, NBTCompound nbtCompound) {
        if(value.getLocation().isPresent()){
            Location loc = value.getLocation().get();
            nbtCompound.setString("world", loc.getWorld().getName());
            nbtCompound.setInteger("x", loc.getBlockX());
            nbtCompound.setInteger("y", loc.getBlockY());
            nbtCompound.setInteger("z", loc.getBlockZ());
        }
    }

    @Override
    public SafeBlockLocation parse(PersistenceStorage persistenceStorage, NBTCompound nbtCompound) {
        World world = Bukkit.getWorld(nbtCompound.getString("world"));
        if(world == null)return null;
        return new SafeBlockLocation(new Location(world, nbtCompound.getInteger("x"), nbtCompound.getInteger("y"), nbtCompound.getInteger("z")));
    }

}
