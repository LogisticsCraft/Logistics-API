package com.logisticscraft.logisticsapi.event;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.logisticscraft.logisticsapi.block.LogisticBlock;

import de.tr7zw.itemnbtapi.NBTCompound;
import lombok.Getter;

public class LogisticBlockSavingEvent extends Event {
    public static final HandlerList handlers = new HandlerList();
    
    @Getter
    private final Location location;
    @Getter
    private final LogisticBlock logisticblock;
    @Getter
    private final NBTCompound savedata;

    public LogisticBlockSavingEvent(Location location, LogisticBlock logisticblock, NBTCompound savedata) {
        super();
        this.location = location;
        this.logisticblock = logisticblock;
        this.savedata = savedata;
    }

    public Block getBlock(){
        return location.getBlock();
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }   
    
    public static HandlerList getHandlerList() {
        return handlers;
    }

}