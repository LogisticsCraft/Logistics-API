package com.logisticscraft.logisticsapi.rewrite.event;

import com.logisticscraft.logisticsapi.rewrite.block.LogisticBlock;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LogisticBlockRegisterEvent extends Event {

    @Getter
    private static final HandlerList handlerList = new HandlerList();

    @Getter
    private final Location location;
    @Getter
    private final LogisticBlock logisticBlock;

    public LogisticBlockRegisterEvent(Location location, LogisticBlock logisticBlock) {
        super();
        this.location = location;
        this.logisticBlock = logisticBlock;
    }

    public Block getBlock() {
        return location.getBlock();
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}
