package com.logisticscraft.logisticsapi.rewrite.event;

import com.logisticscraft.logisticsapi.rewrite.block.LogisticBlock;
import com.logisticscraft.logisticsapi.rewrite.data.SafeBlockLocation;
import lombok.Getter;
import org.bukkit.event.HandlerList;

public class LogisticBlockRegisterEvent extends LogisticBlockEvent {

    @Getter
    private static final HandlerList handlerList = new HandlerList();

    public LogisticBlockRegisterEvent(SafeBlockLocation safeBlockLocation, LogisticBlock logisticBlock) {
        super(safeBlockLocation, logisticBlock);
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}