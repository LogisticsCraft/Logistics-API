package com.logisticscraft.logisticsapi.event;

import com.logisticscraft.logisticsapi.block.LogisticBlock;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.event.HandlerList;

public class LogisticBlockLoadEvent extends LogisticBlockEvent {

    @Getter
    private static final HandlerList handlerList = new HandlerList();

    public LogisticBlockLoadEvent(Location location, LogisticBlock logisticBlock) {
        super(location, logisticBlock);
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}
