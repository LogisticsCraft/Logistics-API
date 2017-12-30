package com.logisticscraft.logisticsapi.listeners;

import com.logisticscraft.logisticsapi.block.LogisticBlock;
import com.logisticscraft.logisticsapi.block.LogisticBlockCache;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

import javax.inject.Inject;
import java.util.Map.Entry;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ChunkListener implements Listener {

    @Inject
    private LogisticBlockCache blockCache;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChunkLoad(ChunkLoadEvent event) {
        blockCache.loadSavedBlocks(event.getChunk());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChunkUnload(ChunkUnloadEvent event) {
        for (Entry<Location, LogisticBlock> data : blockCache.getLogisticBlocksInChunk(event.getChunk()))
            blockCache.unloadLogisticBlock(data.getKey(), true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWorldLoad(WorldLoadEvent event) {
        blockCache.registerWorld(event.getWorld());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onWorldUnload(WorldUnloadEvent event) {
        blockCache.unregisterWorld(event.getWorld());
    }

}
