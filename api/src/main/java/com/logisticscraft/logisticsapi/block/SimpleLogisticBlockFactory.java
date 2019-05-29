package com.logisticscraft.logisticsapi.block;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Supplier;

/**
 * Simple implementation of {@link LogisticBlockFactory} based on {@link Supplier<LogisticBlock>}.
 */
@RequiredArgsConstructor
public class SimpleLogisticBlockFactory implements LogisticBlockFactory {

    @NonNull protected final Supplier<LogisticBlock> blockFactory;

    @Override
    public LogisticBlock onPlace(final Player player, final ItemStack item, final Location location) {
        return blockFactory.get();
    }

    @Override
    public LogisticBlock onLoad() {
        return blockFactory.get();
    }
}
