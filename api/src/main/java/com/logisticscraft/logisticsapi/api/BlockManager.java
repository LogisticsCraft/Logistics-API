package com.logisticscraft.logisticsapi.api;

import com.logisticscraft.logisticsapi.block.LogisticBlockFactory;
import com.logisticscraft.logisticsapi.block.LogisticBlock;
import com.logisticscraft.logisticsapi.block.LogisticBlockTypeRegister;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;

import javax.inject.Inject;

/**
 * Manages the registered LogisticBlock types.
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class BlockManager {

    @Inject
    private LogisticBlockTypeRegister typeRegister;

    /**
     * Registers a new block type into the LogisticAPI block registry.
     *
     * @param plugin  the plugin that is trying to register the block
     * @param name    the name of the block
     * @param block   the block class
     * @param factory the block factory
     */
    public void registerLogisticBlock(@NonNull Plugin plugin, @NonNull String name, @NonNull Class<? extends LogisticBlock> block, @NonNull LogisticBlockFactory factory) {
        typeRegister.registerLogisticBlock(plugin, name, block, factory);
    }

}
