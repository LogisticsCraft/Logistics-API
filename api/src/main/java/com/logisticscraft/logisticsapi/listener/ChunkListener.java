package com.logisticscraft.logisticsapi.listener;

import com.logisticscraft.logisticsapi.block.LogisticBlockCache;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

import javax.inject.Inject;

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
        blockCache.getCachedLogisticBlocksInChunk(event.getChunk())
                .forEach((location, block) -> blockCache.unloadLogisticBlock(location, true));
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
