package com.logisticscraft.logisticsapi.listeners;

import com.logisticscraft.logisticsapi.LogisticsApi;
import com.logisticscraft.logisticsapi.block.LogisticBlock;
import com.logisticscraft.logisticsapi.block.LogisticBlockCache;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class BlockListener implements Listener {

    @Inject
    private LogisticBlockCache blockCache;
    private HashSet<UUID> doubleRightClickcooldown = new HashSet<>();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent event) {
        LogisticBlock block = blockCache.getLoadedLogisticBlockAt(event.getBlock().getLocation());
        if (block != null) {
            block.onPlayerBreak(event);
            if (!event.isCancelled()) {
                blockCache.unloadLogisticBlock(event.getBlock().getLocation(), false);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            LogisticBlock block = blockCache.getLoadedLogisticBlockAt(event.getClickedBlock().getLocation());
            if (block != null) {
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (doubleRightClickcooldown.contains(event.getPlayer().getUniqueId())) return;
                    block.onRightClick(event);
                    doubleRightClickcooldown.add(event.getPlayer().getUniqueId());
                    Bukkit.getScheduler().runTaskLater(LogisticsApi.getInstance(), () -> {
                        doubleRightClickcooldown.remove(event.getPlayer().getUniqueId());
                    }, 1);
                } else {
                    block.onLeftClick(event);
                }
            }
        }
    }

}
