package com.logisticscraft.logisticsapi.block;

import com.logisticscraft.logisticsapi.LogisticsApi;
import com.logisticscraft.logisticsapi.utils.ReflectionUtils;
import lombok.NonNull;
import lombok.Synchronized;
import org.bukkit.scheduler.BukkitRunnable;

import javax.inject.Inject;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Manages the @Ticking annotations into LogisticBlocks.
 */
public class LogisticTickManager {

    private HashMap<Class<? extends LogisticBlock>, HashMap<Method, Integer>> classCache = new HashMap<>();
    private HashSet<LogisticBlock> trackedBlocks = new HashSet<>();
    private long tick = 0;

    @Inject
    LogisticTickManager(LogisticsApi plugin) {
        new BukkitRunnable(){

            @Override
            public void run() {
                onTick();
            }
        }.runTaskTimer(plugin, 1, 1);
    }

    /**
     * Called every game tick by the server scheduler
     */
    private void onTick() {
        tick++;
        for (LogisticBlock logisticBlock : trackedBlocks) {
            HashMap<Method, Integer> tickMap = classCache.get(logisticBlock.getClass());
            for (Method method : tickMap.keySet()) {
                if (tick % tickMap.get(method) == 0) {
                    try {
                        method.invoke(logisticBlock);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Ticking {
        int ticks();
    }

    @Synchronized
    public void addTickingBlock(@NonNull LogisticBlock block) {
        if (classCache.containsKey(block.getClass())) {
            trackedBlocks.add(block);
        }
    }

    @Synchronized
    public void removeTickingBlock(@NonNull LogisticBlock block) {
        trackedBlocks.remove(block);
    }

    @Synchronized
    public void registerLogisticBlockClass(@NonNull Class<? extends LogisticBlock> clazz) {
        if (!classCache.containsKey(clazz)) {
            Collection<Method> methods = ReflectionUtils.getMethodsRecursively(clazz, LogisticBlock.class);
            HashMap<Method, Integer> tickingMethods = new HashMap<>();
            for (Method method : methods) {
                Ticking ticking = method.getAnnotation(Ticking.class);
                if (ticking != null && method.getParameterCount() == 0) {
                    tickingMethods.put(method, ticking.ticks());
                }
            }
            if (tickingMethods.size() > 0) {
                classCache.put(clazz, tickingMethods);
            }
        }
    }

}
