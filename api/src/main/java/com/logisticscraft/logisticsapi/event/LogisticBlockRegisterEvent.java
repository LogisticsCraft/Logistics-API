package com.logisticscraft.logisticsapi.event;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.logisticscraft.logisticsapi.rewrite.block.LogisticBlock;

import de.tr7zw.itemnbtapi.NBTCompound;
import lombok.Getter;

public class LogisticBlockRegisterEvent extends Event {
    public static final HandlerList handlers = new HandlerList();
	
    @Getter
	private final Location location;
    @Getter
	private final LogisticBlock logisticblock;
    @Getter
    private final NBTCompound blockdata;

	public LogisticBlockRegisterEvent(Location location, LogisticBlock logisticblock, NBTCompound blockdata) {
        super();
        this.location = location;
        this.logisticblock = logisticblock;
        this.blockdata = blockdata;
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
