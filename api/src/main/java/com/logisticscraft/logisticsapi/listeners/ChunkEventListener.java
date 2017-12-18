package com.logisticscraft.logisticsapi.listeners;

import com.logisticscraft.logisticsapi.block.LogisticBlock;
import com.logisticscraft.logisticsapi.block.LogisticBlockCache;
import com.logisticscraft.logisticsapi.block.LogisticBlockStorage;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import javax.inject.Inject;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ChunkEventListener implements Listener {

    @Inject
    private LogisticBlockStorage blockStorage;

    @Inject
    private LogisticBlockCache blockCache;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChunkLoad(ChunkLoadEvent event) {
        blockStorage.getLogisticBlocksInChunk(event.getChunk()).forEach(block -> blockCache.loadLogisticBlock(block));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChunkUnload(ChunkUnloadEvent event) {

        blockCache.unloadLogisticBlock();
    }

}
