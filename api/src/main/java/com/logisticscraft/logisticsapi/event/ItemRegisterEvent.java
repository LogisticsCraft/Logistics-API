package com.logisticscraft.logisticsapi.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.logisticscraft.logisticsapi.item.LogisticItem;

import lombok.Getter;
import lombok.NonNull;

public class ItemRegisterEvent extends Event {

    @Getter
    private static final HandlerList handlerList = new HandlerList();
    
    @Getter
    private LogisticItem item;
    
    public ItemRegisterEvent(@NonNull LogisticItem item) {
        this.item = item;
    }
    
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
    
}
