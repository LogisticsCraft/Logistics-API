package com.logisticscraft.logisticsapi.items;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import com.logisticscraft.logisticsapi.data.LogisticKey;
import com.logisticscraft.logisticsapi.utils.Tracer;

import de.tr7zw.itemnbtapi.NBTItem;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class LogisticItemRegister {

    private Map<LogisticKey, LogisticItem> itemTypes = new HashMap<>();
	
    public void registerLogisticItem(@NotNull LogisticItem item){
    	if(itemTypes.putIfAbsent(item.getKey(), item) == null){
    		Tracer.debug("Item register: " + item.getKey());
    	}else{
    		Tracer.warn("Trying to reregister kown Item: " + item.getKey());
    	}
    }
    
    public Optional<LogisticItem> getLogisticItem(String key){
    	return Optional.ofNullable(itemTypes.get(new LogisticKey(key)));
    }
    
    public Optional<LogisticItem> getLogisticItem(LogisticKey key){
    	return Optional.ofNullable(itemTypes.get(key));
    }
    
    public Optional<LogisticItem> getLogisticItem(ItemStack item){
    	NBTItem nbtItem = new NBTItem(item);
    	if(!nbtItem.hasKey("itemid"))return Optional.empty();
    	return Optional.ofNullable(itemTypes.get(new LogisticKey(nbtItem.getString("itemid"))));
    }
    
}
