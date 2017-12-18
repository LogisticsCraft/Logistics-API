package com.logisticscraft.logisticsapi.block;

import com.logisticscraft.logisticsapi.persistence.PersistenceStorage;
import com.logisticscraft.logisticsapi.settings.DataFolder;
import de.tr7zw.itemnbtapi.NBTCompound;
import de.tr7zw.itemnbtapi.NBTFile;
import de.tr7zw.itemnbtapi.NBTList;
import de.tr7zw.itemnbtapi.NBTType;
import org.bukkit.Chunk;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

public class LogisticBlockStorage {

    @Inject
    private PersistenceStorage persistence;

    @Inject
    @DataFolder
    private File dataFolder;

    private NBTFile nbtFile;

    public LogisticBlockStorage() throws IOException {
        nbtFile = new NBTFile(new File(dataFolder, "blocks.nbt"));
    }

    public Set<LogisticBlock> getLogisticBlocksInChunk(Chunk chunk) {
        NBTCompound worldData = nbtFile.getCompound(chunk.getWorld().getName());
        if (worldData == null) {
            return Collections.emptySet();
        }
        NBTList chunkData = worldData.getList(chunk.getX() + "," + chunk.getZ(), NBTType.NBTTagCompound);
        if (chunkData == null) {
            return Collections.emptySet();
        }
        // TODO: iterate list?
    }

    public void saveLogisticBlock(LogisticBlock logisticBlock) {
        persistence.saveFieldData(logisticBlock, );
    }

}
