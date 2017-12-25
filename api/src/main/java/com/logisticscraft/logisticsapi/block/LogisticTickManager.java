package com.logisticscraft.logisticsapi.block;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import com.logisticscraft.logisticsapi.event.LogisticBlockLoadEvent;
import com.logisticscraft.logisticsapi.utils.ReflectionUtils;

public class LogisticTickManager implements Listener{

	public LogisticTickManager(Plugin plugin){
		Bukkit.getScheduler().runTaskTimer(plugin, () -> onTick(), 1, 1);
	}
	
    @Inject
    private LogisticBlockCache blockCache;
	
	private HashMap<Class<? extends LogisticBlock>, HashMap<Method, Integer>> classCache = new HashMap<>();
	private long tick = 0;
	
	public void onTick(){
		tick++;
		Map<Chunk, Map<Location, LogisticBlock>> blockMap = blockCache.getAllLogisticBlocks();
		for(Chunk chunk : blockMap.keySet()){
			for(LogisticBlock logisticBlock : blockMap.get(chunk).values()){
				if(classCache.containsKey(logisticBlock.getClass())){
					HashMap<Method, Integer> tickMap = classCache.get(logisticBlock.getClass());
					for(Method method : tickMap.keySet()){
						if(tick % tickMap.get(method) == 0){
							try{
								method.invoke(logisticBlock);
							}catch(Exception ex){
								ex.printStackTrace();
							}
						}
					}
				}
			}
		}
	}
	
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
	public
    @interface Ticking {
        int ticks();
    }
    
    @EventHandler //TODO: Replace with call from the Block Register
    public void onLogisticBlockPlace(LogisticBlockLoadEvent event){
    	if(!classCache.containsKey(event.getLogisticBlock().getClass())){
    		Class<? extends LogisticBlock> clazz = event.getLogisticBlock().getClass();
    		Collection<Method> methods = ReflectionUtils.getMethodsRecursively(clazz, LogisticBlock.class);
    		HashMap<Method, Integer> tickingMethods = new HashMap<>();
    		for(Method method : methods){
    			Ticking ticking = method.getAnnotation(Ticking.class);
    			if(ticking != null && method.getParameterCount() == 0){
    				tickingMethods.put(method, ticking.ticks());
    			}
    		}
    		classCache.put(clazz, tickingMethods);
    	}
    }
	
}
