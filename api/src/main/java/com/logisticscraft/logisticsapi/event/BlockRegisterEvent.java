package com.logisticscraft.logisticsapi.event;

import com.logisticscraft.logisticsapi.block.LogisticBlock;
import com.logisticscraft.logisticsapi.data.LogisticKey;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BlockRegisterEvent extends Event {

    @Getter
    private static final HandlerList handlerList = new HandlerList();

    @Getter
    private LogisticKey key;
    @Getter
    private Class<? extends LogisticBlock> blockClass;

    public BlockRegisterEvent(@NonNull LogisticKey key, @NonNull Class<? extends LogisticBlock> clazz) {
        this.key = key;
        this.blockClass = clazz;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
