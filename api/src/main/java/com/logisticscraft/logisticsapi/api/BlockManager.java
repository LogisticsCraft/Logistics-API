package com.logisticscraft.logisticsapi.api;

import javax.inject.Inject;

import org.bukkit.plugin.Plugin;

import com.logisticscraft.logisticsapi.block.BlockFactory;
import com.logisticscraft.logisticsapi.block.LogisticBlock;
import com.logisticscraft.logisticsapi.block.LogisticBlockTypeRegister;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class BlockManager {

    @Inject
    private LogisticBlockTypeRegister typeRegister;
    
    public void registerLogisticBlock(@NonNull Plugin plugin, @NonNull String name, @NonNull Class<? extends LogisticBlock> block, @NonNull BlockFactory factory) {
        typeRegister.registerLogisticBlock(plugin, name, block, factory);
    }
    
}
