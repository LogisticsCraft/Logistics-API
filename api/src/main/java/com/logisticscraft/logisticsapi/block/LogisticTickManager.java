package com.logisticscraft.logisticsapi.block;

import lombok.NonNull;
import lombok.Synchronized;
import org.bukkit.plugin.Plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface LogisticTickManager extends Runnable, AutoCloseable {

    void start(@NonNull Plugin plugin);

    @Synchronized
    void addTickingBlock(@NonNull LogisticBlock block);

    @Synchronized
    void removeTickingBlock(@NonNull LogisticBlock block);

    @Synchronized
    void registerLogisticBlockClass(@NonNull Class<? extends LogisticBlock> clazz);

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Ticking {

        int ticks();
    }
}
