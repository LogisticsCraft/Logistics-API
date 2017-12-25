package com.logisticscraft.logisticsapi.block;

import com.logisticscraft.logisticsapi.LogisticsApi;
import com.logisticscraft.logisticsapi.utils.ReflectionUtils;
import lombok.NonNull;
import lombok.Synchronized;
import lombok.val;
import org.bukkit.Bukkit;

import javax.inject.Inject;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;

public class LogisticTickManager {

    private HashMap<Class<? extends LogisticBlock>, HashMap<Method, Integer>> classCache = new HashMap<>();
    private HashSet<LogisticBlock> trackedBlocks = new HashSet<>();
    private long tick = 0;

    @Inject
    LogisticTickManager(LogisticsApi plugin) {
        Bukkit.getScheduler().runTaskTimer(plugin, this::onTick, 1, 1);
    }

    private void onTick() {
        tick++;
        for (val logisticBlock : trackedBlocks) {
            val tickMap = classCache.get(logisticBlock.getClass());
            for (val method : tickMap.keySet()) {
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
            val methods = ReflectionUtils.getMethodsRecursively(clazz, LogisticBlock.class);
            val tickingMethods = new HashMap<Method, Integer>();
            for (val method : methods) {
                val ticking = method.getAnnotation(Ticking.class);
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
