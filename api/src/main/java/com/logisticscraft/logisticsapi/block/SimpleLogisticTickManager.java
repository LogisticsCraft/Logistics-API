package com.logisticscraft.logisticsapi.block;

import lombok.*;
import lombok.experimental.NonFinal;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

import static com.logisticscraft.logisticsapi.util.ReflectionUtils.getMethodsRecursively;
import static com.logisticscraft.logisticsapi.util.ReflectionUtils.toInstanceConsumer;

/**
 * Manages the @Ticking annotations into LogisticBlocks.
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class SimpleLogisticTickManager implements LogisticTickManager {

    private Lock runLock = new ReentrantLock();

    private BukkitTask task;

    private Map<Class<? extends LogisticBlock>, TickedCall> blockHandlers = new ConcurrentHashMap<>();
    private AtomicLong tick = new AtomicLong();

    @Override
    public void start(@NonNull final Plugin plugin) {
        runLock.lock();
        try {
            if (task == null) task = plugin.getServer().getScheduler().runTaskTimer(plugin, this, 20L, 1L);
        } finally {
            runLock.unlock();
        }
    }

    @Override
    public void close() {
        runLock.lock();
        try {
            if (task != null) task.cancel();
        } finally {
            runLock.unlock();
        }
    }

    /**
     * Called every game tick by the server scheduler
     */
    @Override
    public void run() {
        val tick = this.tick.getAndIncrement();

        for (final TickedCall handler : blockHandlers.values()) handler.tick(tick);
    }

    @Override
    @Synchronized
    public void addTickingBlock(@NonNull LogisticBlock block) {
        getLogisticBlockClass(block.getClass()).registerBlock(block);
    }

    @Override
    public void removeTickingBlock(@NonNull LogisticBlock block) {
        val handler = blockHandlers.get(block.getClass());
        if (handler != null) handler.unregisterBlock(block);
    }

    @Override
    public void registerLogisticBlockClass(@NonNull Class<? extends LogisticBlock> blockType) {
        getLogisticBlockClass(blockType);
    }

    protected TickedCall getLogisticBlockClass(@NonNull Class<? extends LogisticBlock> blockType) {
        return blockHandlers.computeIfAbsent(blockType, type -> getMethodsRecursively(blockType, LogisticBlock.class)
                .stream()
                .filter(method -> method.getParameterCount() == 0)
                .filter(method -> method.isAnnotationPresent(Ticking.class))
                .findFirst()
                .map(method -> new SimpleTickedCall(
                        toInstanceConsumer(method, blockType), method.getAnnotation(Ticking.class).ticks()
                )).orElseThrow(
                        () -> new IllegalArgumentException("No method annotated as @Ticking found in " + blockType)
                )
        );
    }

    protected interface TickedCall {

        void tick(long tick);

        void registerBlock(@NonNull LogisticBlock block);

        void unregisterBlock(@NonNull LogisticBlock block);
    }

    @Value
    @NonFinal protected class SimpleTickedCall implements TickedCall {

        @NonNull @Getter(AccessLevel.NONE) Consumer<LogisticBlock> blockConsumer;
        @NonNull protected long ticks;
        @NonNull Collection<LogisticBlock> blocks = ConcurrentHashMap.newKeySet();

        @Override
        public void tick(final long tick) {
            if (tick % ticks == 0) for (val block : blocks) blockConsumer.accept(block);
        }

        @Override
        public void registerBlock(final LogisticBlock block) {
            blocks.add(block);
        }

        @Override
        public void unregisterBlock(final LogisticBlock block) {
            blocks.remove(block);
        }
    }
}
