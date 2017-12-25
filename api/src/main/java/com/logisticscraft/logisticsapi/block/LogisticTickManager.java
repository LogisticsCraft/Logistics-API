package com.logisticscraft.logisticsapi.block;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.logisticscraft.logisticsapi.utils.ReflectionUtils;

import lombok.Synchronized;

public class LogisticTickManager{

	public LogisticTickManager(Plugin plugin){
		Bukkit.getScheduler().runTaskTimer(plugin, () -> onTick(), 1, 1);
	}

	private HashMap<Class<? extends LogisticBlock>, HashMap<Method, Integer>> classCache = new HashMap<>();
	private HashSet<LogisticBlock> trackedBlocks = new HashSet<>();
	private long tick = 0;

	public void onTick(){
		tick++;
		for(LogisticBlock logisticBlock : trackedBlocks){
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

	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public
	@interface Ticking {
		int ticks();
	}

	@Synchronized
	public void addTickingBlock(LogisticBlock block){
		if(classCache.containsKey(block.getClass())){
			trackedBlocks.add(block);
		}
	}

	@Synchronized
	public void removeTickingBlock(LogisticBlock block){
		trackedBlocks.remove(block);
	}

	@Synchronized
	public void registerLogisticBlockClass(Class<? extends LogisticBlock> clazz){
		if(!classCache.containsKey(clazz)){
			Collection<Method> methods = ReflectionUtils.getMethodsRecursively(clazz, LogisticBlock.class);
			HashMap<Method, Integer> tickingMethods = new HashMap<>();
			for(Method method : methods){
				Ticking ticking = method.getAnnotation(Ticking.class);
				if(ticking != null && method.getParameterCount() == 0){
					tickingMethods.put(method, ticking.ticks());
				}
			}
			if(tickingMethods.size() > 0)
				classCache.put(clazz, tickingMethods);
		}
	}

}
