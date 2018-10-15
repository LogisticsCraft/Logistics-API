package com.logisticscraft.logisticsapi.block;

import com.logisticscraft.logisticsapi.util.ReflectionUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Synchronized;
import org.bukkit.scheduler.BukkitRunnable;

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
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class LogisticTickManager extends BukkitRunnable {

    private HashMap<Class<? extends LogisticBlock>, HashMap<Method, Integer>> classCache = new HashMap<>();
    private HashSet<LogisticBlock> trackedBlocks = new HashSet<>();
    private long tick = 0;

    /**
     * Called every game tick by the server scheduler
     */
    @Override
    public void run() {
        tick++;
        for (LogisticBlock logisticBlock : trackedBlocks) {
            HashMap<Method, Integer> tickMap = classCache.get(logisticBlock.getClass());
            tickMap.forEach((method, current) -> {
                if (tick % current == 0) {
                    try {
                        method.invoke(logisticBlock);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
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
            methods.forEach(method -> {
                Ticking ticking = method.getAnnotation(Ticking.class);
                if (ticking != null && method.getParameterCount() == 0) {
                    tickingMethods.put(method, ticking.ticks());
                }
            });
            if (tickingMethods.size() > 0) {
                classCache.put(clazz, tickingMethods);
            }
        }
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Ticking {

        int ticks();
    }
}
