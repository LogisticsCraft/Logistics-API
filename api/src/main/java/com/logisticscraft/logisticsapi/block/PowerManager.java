package com.logisticscraft.logisticsapi.block;

import java.util.HashMap;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.logisticscraft.logisticsapi.event.LogisticBlockRegisterEvent;
import com.logisticscraft.logisticsapi.event.LogisticBlockUnregisterEvent;

public class PowerManager implements Listener{

    private static PowerManager instance;
    private HashMap<Integer, Long> powerMapping = new HashMap<>();
    
    public PowerManager(){
        if(instance == null)instance = this;
    }
    
    public static PowerManager getInstance(){
        return instance;
    }
    
    public Long getPower(int id){
        return powerMapping.get(id);
    }
    
    public void setPower(int id, long power){
        powerMapping.put(id, power);
    }
    
    @EventHandler(priority=EventPriority.LOWEST)
    public void registerBlock(LogisticBlockRegisterEvent event){
        if(event.getBlockdata().hasKey("power")){
            powerMapping.put(event.getLogisticblock().getId(), event.getBlockdata().getLong("power"));
            return;
        }
        powerMapping.put(event.getLogisticblock().getId(), 0l);
    }
    
    @EventHandler(priority=EventPriority.HIGHEST)
    public void unregisterBlock(LogisticBlockUnregisterEvent event){
        if(powerMapping.containsKey(event.getLogisticblock().getId())){
            event.getSavedata().setLong("power", powerMapping.get(event.getLogisticblock().getId()));
        }
        powerMapping.remove(event.getLogisticblock().getId());
    }
    
}
