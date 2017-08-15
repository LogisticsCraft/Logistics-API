package com.logisticscraft.logisticsapi.event;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.logisticscraft.logisticsapi.LogisticObject;

public class LogisticObjectRegisterEvent extends Event {
    public static final HandlerList handlers = new HandlerList();
	
	private final Location location;
	private final LogisticObject object;

	public LogisticObjectRegisterEvent(Location location, LogisticObject object) {
        super();
        this.location = location;
        this.object = object;
    }

	public Block getBlock(){
	    return location.getBlock();
	}
	
    public Location getLocation() {
        return location;
    }

    public LogisticObject getLogisticObject() {
        return object;
    }

    @Override
	public HandlerList getHandlers() {
		return handlers;
	}	
	
	public static HandlerList getHandlerList() {
		return handlers;
	}



}