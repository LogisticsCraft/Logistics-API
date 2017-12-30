package com.logisticscraft.logisticsapi.block;

import com.logisticscraft.logisticsapi.data.LogisticKey;
import com.logisticscraft.logisticsapi.data.SafeBlockLocation;
import com.logisticscraft.logisticsapi.event.LogisticBlockLoadEvent;
import com.logisticscraft.logisticsapi.event.LogisticBlockSaveEvent;
import com.logisticscraft.logisticsapi.event.LogisticBlockUnloadEvent;
import com.logisticscraft.logisticsapi.persistence.PersistenceStorage;
import com.logisticscraft.logisticsapi.utils.Tracer;
import lombok.NonNull;
import lombok.Synchronized;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;

import javax.inject.Inject;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages all the loaded LogisticBlock s in the server.
 * This is an internal class, not to be confused with the public API.
 */
public class LogisticBlockCache {

    private PluginManager pluginManager;
    private LogisticBlockTypeRegister typeRegister;
    private LogisticTickManager tickManager;
    private PersistenceStorage persistence;

    private Map<World, LogisticWorldStorage> worldStorage;
    private Map<Chunk, Map<Location, LogisticBlock>> logisticBlocks;

    @Inject
    LogisticBlockCache(PluginManager pluginManager, LogisticBlockTypeRegister typeRegister,
                       LogisticTickManager tickManager, PersistenceStorage persistence) {
        this.pluginManager = pluginManager;
        this.typeRegister = typeRegister;
        this.tickManager = tickManager;
        this.persistence = persistence;

        worldStorage = new ConcurrentHashMap<>();
        logisticBlocks = new ConcurrentHashMap<>();
    }

    /**
     * Loads a LogisticBlock, this method should be called only when a new block is placed or when
     * a stored block is loaded from the disk.
     *
     * @param block the block
     * @throws IllegalArgumentException if the given block location isn't loaded
     */
    public void loadLogisticBlock(@NonNull final LogisticBlock block) {
        if (!typeRegister.isBlockRegistert(block)) {
            throw new IllegalArgumentException("The class " + block.getClass().getName() + " is not registert!");
        }
        Location location = block.getLocation().getLocation()
                .orElseThrow(() -> new IllegalArgumentException("The provided block must be loaded!"));
        Chunk chunk = location.getChunk();
        pluginManager.callEvent(new LogisticBlockLoadEvent(location, block));
        if (logisticBlocks.computeIfAbsent(chunk, k -> new ConcurrentHashMap<>())
                .putIfAbsent(location, block) == null) {
            tickManager.addTickingBlock(block);
            Tracer.debug("Block loaded: " + location.toString());
        } else {
            Tracer.warn("Trying to load a block at occupied location: " + location.toString());
        }
    }

    /**
     * Unloads a LogisticBlock, this method should be called only when a block is destroyed or when
     * a chunk is unloaded.
     *
     * @param location the block location
     * @param save     if the block should be saved
     * @throws IllegalArgumentException if the given location isn't loaded
     */
    @Synchronized
    public void unloadLogisticBlock(@NonNull final Location location, boolean save) {
        Chunk chunk = location.getChunk();
        if (!chunk.isLoaded()) throw new IllegalArgumentException("The provided location must be loaded!");
        Map<Location, LogisticBlock> loadedBlocksInChunk = logisticBlocks.get(chunk);
        if (loadedBlocksInChunk == null) {
            Tracer.warn("Attempt to unregister an unloaded LogisticBlock: " + location.toString());
            return;
        }
        LogisticBlock logisticBlock = loadedBlocksInChunk.get(location);
        if (logisticBlock == null) {
            Tracer.warn("Attempt to unregister an unknown LogisticBlock: " + location.toString());
            return;
        }
        tickManager.removeTickingBlock(logisticBlock);
        if (save) {
            pluginManager.callEvent(new LogisticBlockSaveEvent(location, logisticBlock));
            worldStorage.get(location.getWorld()).saveLogisticBlock(logisticBlock);
        } else {
            pluginManager.callEvent(new LogisticBlockUnloadEvent(location, logisticBlock));
            worldStorage.get(location.getWorld()).removeLogisticBlock(logisticBlock);
        }
        logisticBlocks.get(location.getChunk()).remove(location);
    }

    /**
     * Checks if a LogisticBlock is loaded at the given LOADED location.
     *
     * @param location the location to check
     * @return true if a LogisticBlock is loaded at the given location, false otherwise
     * @throws IllegalArgumentException if the given location isn't loaded
     */
    @Synchronized
    public boolean isLogisticBlockLoadedAt(@NonNull final Location location) {
        return getLoadedLogisticBlockAt(location) != null;
    }

    /**
     * Checks if the given LogisticBlock is loaded.
     *
     * @param block the location to check
     * @return true if a LogisticBlock is loaded at the given location, false otherwise
     * @throws IllegalArgumentException if the given block location isn't loaded
     */
    @Synchronized
    public boolean isLogisticBlockLoaded(@NonNull final LogisticBlock block) {
        Optional<Location> location = block.getLocation().getLocation();
        if (!location.isPresent()) throw new IllegalArgumentException("The provided block must be loaded!");
        return block.getLocation().getLocation() != null && block.equals(getLoadedLogisticBlockAt(location.get()));
    }

    /**
     * Get the LOADED LogisticBlock at the given location
     *
     * @param location the location
     * @return the LogisticBlock
     * @throws IllegalArgumentException if the given location isn't loaded
     */
    @Synchronized
    public LogisticBlock getLoadedLogisticBlockAt(@NonNull final Location location) {
        Chunk chunk = location.getChunk();
        if (!chunk.isLoaded()) throw new IllegalArgumentException("The provided location must be loaded!");
        Map<Location, LogisticBlock> loadedBlockInChunk = logisticBlocks.get(chunk);
        if (loadedBlockInChunk == null) return null;
        return loadedBlockInChunk.get(location);
    }

    /**
     * Get the LogisticBlocks in the given LOADED chunk
     *
     * @param chunk the chunk
     * @return the LogisticBlock set
     * @throws IllegalArgumentException if the given chunk isn't loaded
     */
    @Synchronized
    public Set<Entry<Location, LogisticBlock>> getLogisticBlocksInChunk(@NonNull final Chunk chunk) {
        if (!chunk.isLoaded()) throw new IllegalArgumentException("The provided chunk must be loaded!");
        if (!logisticBlocks.containsKey(chunk)) return new HashSet<>();
        return Collections.unmodifiableSet(logisticBlocks.get(chunk).entrySet());
    }

    @Synchronized
    public void loadSavedBlocks(@NonNull Chunk chunk) {
        LogisticWorldStorage storage = worldStorage.get(chunk.getWorld());
        if (storage == null) return;
        Optional<Set<? extends LogisticBlock>> blocks = storage.getSavedLogisticBlocksInChunk(chunk);
        if (blocks.isPresent()) {
            for (LogisticBlock block : blocks.get()) {
                loadLogisticBlock(block);
            }
        }
    }

    @Synchronized
    public Set<Chunk> getChunksWithLogisticBlocksInWorld(@NonNull World world) {
        HashSet<Chunk> chunks = new HashSet<>();
        for (Chunk chunk : logisticBlocks.keySet()) {
            if (chunk.getWorld().equals(world)) chunks.add(chunk);
        }
        return Collections.unmodifiableSet(chunks);
    }

    @Synchronized
    public void registerWorld(@NonNull World world) {
        try {
            worldStorage.put(world, new LogisticWorldStorage(persistence, typeRegister, world));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Synchronized
    public void unregisterWorld(@NonNull World world) {
        if (worldStorage.containsKey(world)) {
            for (Chunk chunk : getChunksWithLogisticBlocksInWorld(world)) {
                for (Entry<Location, LogisticBlock> data : getLogisticBlocksInChunk(chunk))
                    unloadLogisticBlock(data.getKey(), true);
            }
            LogisticWorldStorage storage = worldStorage.get(world);
            try {
                storage.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
            worldStorage.remove(world);
        }
    }

    @Synchronized
    public Map<Chunk, Map<Location, LogisticBlock>> getAllLogisticBlocks() {
        return logisticBlocks;
    }

    public Optional<LogisticBlock> injectData(@NonNull LogisticBlock logisticBlock, @NonNull Location location) {
        if (!typeRegister.isBlockRegistert(logisticBlock)) {
            Tracer.warn("Attempt to inject Data into unknown LogisticBlock Class: " + logisticBlock.getClass().getName());
            return Optional.empty();
        }
        Optional<LogisticKey> key = typeRegister.getKey(logisticBlock);
        if (!key.isPresent()) {
            Tracer.warn("Unable to get Key for class: " + logisticBlock.getClass().getName());
            return Optional.empty();
        }
        try {
            Field idField = LogisticBlock.class.getDeclaredField("typeId");
            idField.setAccessible(true);
            idField.set(logisticBlock, key.get());
            Field locField = LogisticBlock.class.getDeclaredField("location");
            locField.setAccessible(true);
            locField.set(logisticBlock, new SafeBlockLocation(location));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Optional.of(logisticBlock);
    }

    // TODO: Missing: World getter, Point where loading/events/listener are located
}
