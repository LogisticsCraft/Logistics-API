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
import java.util.Optional;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class BlockListener implements Listener {

    @Inject
    private LogisticBlockCache blockCache;
    private HashSet<UUID> doubleRightClickCooldown = new HashSet<>();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent event) {
        Optional<LogisticBlock> block = blockCache.getCachedLogisticBlockAt(event.getBlock().getLocation());
        block.ifPresent(b -> {
            b.onPlayerBreak(event);
            if (!event.isCancelled()) {
                blockCache.unloadLogisticBlock(event.getBlock().getLocation(), false);
            }
        });
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Optional<LogisticBlock> oblock = blockCache.getCachedLogisticBlockAt(event.getClickedBlock().getLocation());
            oblock.ifPresent(block -> {
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (doubleRightClickCooldown.contains(event.getPlayer().getUniqueId()))
                        return;
                    block.onRightClick(event);
                    doubleRightClickCooldown.add(event.getPlayer().getUniqueId());
                    Bukkit.getScheduler().runTaskLater(LogisticsApi.getInstance(), () -> {
                        doubleRightClickCooldown.remove(event.getPlayer().getUniqueId());
                    }, 1);
                } else {
                    block.onLeftClick(event);
                }
            });
        }
    }

}
