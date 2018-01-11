package com.logisticscraft.logisticsapi.block;

import com.logisticscraft.logisticsapi.data.LogisticKey;
import com.logisticscraft.logisticsapi.data.SafeBlockLocation;
import com.logisticscraft.logisticsapi.persistence.PersistenceStorage;
import com.logisticscraft.logisticsapi.utils.Tracer;
import de.tr7zw.itemnbtapi.NBTCompound;
import de.tr7zw.itemnbtapi.NBTFile;
import lombok.Getter;
import lombok.NonNull;
import lombok.Synchronized;
import org.bukkit.Chunk;
import org.bukkit.World;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class LogisticWorldStorage {

    private final PersistenceStorage persistence;
    private final LogisticBlockTypeRegister register;

    @Getter
    private World world;
    @Getter
    private NBTFile nbtFile;

    public LogisticWorldStorage(@NonNull PersistenceStorage persistance, @NonNull LogisticBlockTypeRegister register, @NonNull final World world) throws IOException {
        this.persistence = persistance;
        this.register = register;
        this.world = world;
        nbtFile = new NBTFile(new File(world.getWorldFolder(), "logisticblocks.nbt"));
        if (!nbtFile.hasKey("chunks"))
            nbtFile.addCompound("chunks");
    }

    public void save() throws IOException {
        nbtFile.save();
    }

    @Synchronized
    public Optional<Set<? extends LogisticBlock>> getSavedLogisticBlocksInChunk(@NonNull final Chunk chunk) {
        NBTCompound chunks = nbtFile.getCompound("chunks");

        NBTCompound chunkData = chunks.getCompound(chunk.getX() + ";" + chunk.getZ());
        if (chunkData == null || chunkData.getKeys().size() == 0) return Optional.empty();

        Set<LogisticBlock> blocks = new HashSet<>();
        for (String key : chunkData.getKeys()) {
            NBTCompound blockData = chunkData.getCompound(key);
            LogisticKey logisticKey = new LogisticKey(blockData.getCompound("typeId").getString("key"));
            Optional<LogisticBlockFactory> factory = register.getFactory(logisticKey);
            if (factory.isPresent()) {
                LogisticBlock block = factory.get().onLoadUnsafe(blockData);
                //TODO: Block onload nbt method
                persistence.loadFields(block, blockData);
                blocks.add(block);
            } else {
                Tracer.warn("Unable to find Factory for key: " + logisticKey);
            }
        }

        if (blocks.isEmpty()) return Optional.empty(); // Just in case
        return Optional.of(blocks);
    }

    @Synchronized
    public void removeLogisticBlock(@NonNull final LogisticBlock logisticBlock) {
        NBTCompound chunks = nbtFile.addCompound("chunks");

        SafeBlockLocation location = logisticBlock.getLocation();
        NBTCompound chunkData = chunks.getCompound(location.getChunkX() + ";" + location.getChunkZ());
        if (chunkData == null) return;

        if (chunkData.hasKey(location.getX() + ";" + location.getY() + ";" + location.getZ())) {
            chunkData.removeKey(location.getX() + ";" + location.getY() + ";" + location.getZ());
        }
        if (chunkData.getKeys().size() == 0) {
            chunks.removeKey(location.getChunkX() + ";" + location.getChunkZ());
        }
    }

    @Synchronized
    public void saveLogisticBlock(@NonNull final LogisticBlock logisticBlock) {
        NBTCompound chunks = nbtFile.getCompound("chunks");

        SafeBlockLocation location = logisticBlock.getLocation();
        NBTCompound chunkData;
        if (chunks.hasKey(location.getChunkX() + ";" + location.getChunkZ())) {
            chunkData = chunks.getCompound(location.getChunkX() + ";" + location.getChunkZ());
        } else {
            chunkData = chunks.addCompound(location.getChunkX() + ";" + location.getChunkZ());
        }
        NBTCompound blockData = chunkData.addCompound(location.getX() + ";" + location.getY() + ";" + location.getZ());
        persistence.saveFields(logisticBlock, blockData);
    }

    //TODO: Loading blocks

}
