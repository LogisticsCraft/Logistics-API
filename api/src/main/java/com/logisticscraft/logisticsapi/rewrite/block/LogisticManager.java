package com.logisticscraft.logisticsapi.rewrite.block;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Chunk;
import org.bukkit.Location;

import com.logisticscraft.logisticsapi.rewrite.utils.Tracer;

import lombok.NonNull;
import lombok.Synchronized;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LogisticManager {

    private ConcurrentHashMap<Chunk, ConcurrentHashMap<Location, LogisticBlock>> logisticBlocks = new ConcurrentHashMap<>();

    public static void registerLogisticBlock(@NonNull final Location location,
            @NonNull final LogisticBlock block) {
        if (logisticBlocks.computeIfAbsent(location.getChunk(), k -> new ConcurrentHashMap<Location, LogisticBlock>())
                .putIfAbsent(location, block) == null){
            Tracer.info("Energy storage registered at " + location.toString() );
        }else{
            Tracer.warn("Trying to register EnergyStorage at occupied location: " + location.toString());
        }
    }

    @Synchronized
    public static void unregisterLogisticBlock(@NonNull final Location location) {
        if(logisticBlocks.get(location.getChunk()).contains(location)){
            //Events?
            logisticBlocks.get(location.getChunk()).remove(location);
        }else{
            Tracer.warn("Attempt to unregister unknown LogisticBlock");
        }
    }

    @Synchronized
    public static boolean isLogisticBlockAt(@NonNull final Location location) {
        return getLogisticBlockAt(location) != null;
    }

    @Synchronized
    public static boolean isLogisticBlockRegistered(@NonNull final LogisticBlock block) {
        if(block.getLocation() == null)return false;
        return block.equals(getLogisticBlockAt(block.getLocation()));
    }

    @Synchronized
    public static LogisticBlock getLogisticBlockAt(@NonNull final Location location) {
        ConcurrentHashMap<Location, LogisticBlock> chunk = logisticBlocks.get(location.getChunk());
        if(chunk == null)return null;
        return chunk.get(location);
    }

    @Synchronized
    public static Set<Entry<Location, LogisticBlock>> getLogisticBlocksinChunk(Chunk chunk){
        if(!logisticBlocks.contains(chunk))return new HashSet<>();
        return Collections.unmodifiableSet(logisticBlocks.get(chunk).entrySet());
    }

    //Missing: World getter, Point where loading/events/listener are located
}
