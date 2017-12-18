package com.logisticscraft.logisticsapi.event;

import com.logisticscraft.logisticsapi.block.LogisticBlock;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.event.HandlerList;

public class LogisticBlockUnloadEvent extends LogisticBlockSaveEvent {

    @Getter
    private static final HandlerList handlerList = new HandlerList();

    public LogisticBlockUnloadEvent(Location location, LogisticBlock logisticBlock) {
        super(location, logisticBlock);
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}
