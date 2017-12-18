package com.logisticscraft.logisticsapi.listeners;

import com.logisticscraft.logisticsapi.block.LogisticBlockCache;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import javax.inject.Inject;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ChunkListener {

    @Inject
    private LogisticBlockCache blockCache;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChunkLoad(ChunkLoadEvent event) {
        blockCache.loadLogisticBlock();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChunkUnload(ChunkUnloadEvent event) {
        blockCache.unloadLogisticBlock();
    }

}
