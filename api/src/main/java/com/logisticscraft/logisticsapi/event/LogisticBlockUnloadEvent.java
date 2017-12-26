package com.logisticscraft.logisticsapi.event;

import com.logisticscraft.logisticsapi.block.LogisticBlock;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.event.HandlerList;

public class LogisticBlockUnloadEvent extends LogisticBlockSaveEvent {

    @Getter
    private static final HandlerList handlerList = new HandlerList();

    public LogisticBlockUnloadEvent(@NonNull Location location, @NonNull LogisticBlock logisticBlock) {
        super(location, logisticBlock);
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}
