package com.logisticscraft.logisticsapi.block;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import javax.inject.Inject;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import com.logisticscraft.logisticsapi.persistence.PersistenceStorage;

import de.tr7zw.itemnbtapi.NBTCompound;
import de.tr7zw.itemnbtapi.NBTFile;
import de.tr7zw.itemnbtapi.NBTList;
import de.tr7zw.itemnbtapi.NBTType;
import lombok.Getter;
import lombok.Synchronized;

public class LogisticWorldStorage {

    @Inject
    private PersistenceStorage persistence;

    @Getter
    private World world;
    @Getter
    private NBTFile nbtFile;

    public LogisticWorldStorage(World world) throws IOException {
        this.world = world;
        nbtFile = new NBTFile(new File(world.getWorldFolder(), "logisticapi.nbt"));
        nbtFile.addCompound("chunks");
    }

    @Synchronized
    public Set<LogisticBlock> getLogisticBlocksInChunk(Chunk chunk) {
        NBTCompound chunks = nbtFile.getCompound("chunks");
        if (chunks == null) {
            return Collections.emptySet();
        }
        NBTCompound chunkData = chunks.getCompound(chunk.getX() + ";" + chunk.getZ());
        if (chunkData == null) {
            return Collections.emptySet();
        }
        for(String key : chunkData.getKeys()){
            NBTCompound blockdata = chunkData.getCompound(key);
            //TODO: Load block using its data. Put location, logisticblocktype, ids what ever into this Compound.
            
        }
    }

    @Synchronized
    public void removeLogisticBlock(LogisticBlock logisticBlock){
        NBTCompound chunks = nbtFile.getCompound("chunks");
        if (chunks == null)return;
        Location loc = logisticBlock.getSafeLocation().getLocation().get();
        NBTCompound chunkData = chunks.getCompound(loc.getChunk().getX() + ";" + loc.getChunk().getZ());
        if (chunkData == null)return;
        if (chunkData.hasKey(loc.getBlockX() + ";" + loc.getBlockY() + ";" + loc.getBlockZ())){
            chunkData.removeKey(loc.getBlockX() + ";" + loc.getBlockY() + ";" + loc.getBlockZ());
        }
        if(chunkData.getKeys().size() == 0){
            chunks.removeKey(loc.getChunk().getX() + ";" + loc.getChunk().getZ());
        }
    }
    
    @Synchronized
    public void saveLogisticBlock(LogisticBlock logisticBlock) {
        persistence.saveFieldData(logisticBlock);
    }

}
