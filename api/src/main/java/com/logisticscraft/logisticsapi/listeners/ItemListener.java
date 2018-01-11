package com.logisticscraft.logisticsapi.listeners;

import java.util.Optional;

import javax.inject.Inject;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

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
        Optional<LogisticItem> logisticItem = itemRegister.getLogisticItem(event.getItemInHand());
        logisticItem.ifPresent(item -> {
            if (item instanceof LogisticBlockItem) {
                ((LogisticBlockItem) item).onPlace(event.getPlayer(), event.getItemInHand(), event.getBlock());
            }
        });
    }

}
