package com.logisticscraft.logisticsapi.listeners;

import java.util.Optional;

import javax.inject.Inject;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.logisticscraft.logisticsapi.item.LogisticBlockItem;
import com.logisticscraft.logisticsapi.item.LogisticItem;
import com.logisticscraft.logisticsapi.item.LogisticItemRegister;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ItemListener implements Listener {

    @Inject
    private LogisticItemRegister itemRegister;

    @EventHandler
    public void onPlacer(BlockPlaceEvent event) {
        if(event.isCancelled())return;
        if(!event.canBuild())return;
        Optional<LogisticItem> logisticItem = itemRegister.getLogisticItem(event.getItemInHand());
        logisticItem.ifPresent(item -> {
            if (item instanceof LogisticBlockItem) {
                ((LogisticBlockItem) item).onPlace(event.getPlayer(), event.getItemInHand(), event.getBlock());
            }
        });
    }

    @EventHandler
    public void onPlace(PlayerInteractEvent event){
        if(event.isCancelled())return;
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
            Optional<LogisticItem> logisticItem = itemRegister.getLogisticItem(event.getItem());
            Block block = event.getClickedBlock().getRelative(event.getBlockFace());
            if(logisticItem.isPresent() && block.getType() == Material.AIR && logisticItem.get() instanceof LogisticBlockItem){
                ((LogisticBlockItem) logisticItem.get()).onPlace(event.getPlayer(), event.getItem(), block);
            }
        }
    }

}
