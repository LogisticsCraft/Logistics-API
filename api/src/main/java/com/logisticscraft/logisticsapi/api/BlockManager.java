package com.logisticscraft.logisticsapi.api;

import com.logisticscraft.logisticsapi.block.LogisticBlock;
import com.logisticscraft.logisticsapi.block.LogisticBlockCache;
import com.logisticscraft.logisticsapi.block.LogisticBlockFactory;
import com.logisticscraft.logisticsapi.block.LogisticBlockTypeRegister;
import com.logisticscraft.logisticsapi.data.LogisticKey;
import com.logisticscraft.logisticsapi.utils.Tracer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import javax.inject.Inject;

import java.util.Map;
import java.util.Optional;

/**
 * Manages the registered LogisticBlock types.
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class BlockManager {

    @Inject
    private LogisticBlockTypeRegister typeRegister;
    @Inject
    private LogisticBlockCache blockCache;

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

    /**
     * Places a logistic block at the given location.
     * TODO: actually place the block? -sgdc3
     *
     * @param location the location
     * @param logisticBlock the logistic block instance
     */
    public void placeLogisticBlock(@NonNull Location location, @NonNull LogisticBlock logisticBlock) {
        Optional<LogisticBlock> block = blockCache.injectData(logisticBlock, location);
        if (!block.isPresent()) {
            Tracer.warn("Unable to place LogisticBlock: " + logisticBlock.getClass().getName());
            return;
        }
        blockCache.loadLogisticBlock(block.get());
    }
    
    public Optional<LogisticBlock> placeLogisticBlock(Player player, @NonNull ItemStack item, @NonNull Location location, @NonNull LogisticKey logisticKey) {
        Optional<LogisticBlockFactory> factory = typeRegister.getFactory(logisticKey);
        if(!factory.isPresent()){
            Tracer.warn("Unable to find factory: " + logisticKey);
            return Optional.empty(); 
        }
        LogisticBlock logisticBlock = factory.get().onPlace(player, item, location);
        Optional<LogisticBlock> block = blockCache.injectData(logisticBlock, location);
        if (!block.isPresent()) {
            Tracer.warn("Unable to place LogisticBlock: " + logisticBlock.getClass().getName());
            return Optional.empty(); 
        }
        blockCache.loadLogisticBlock(block.get());
        return block;
    }
    
    public Map<Chunk, Map<Location, LogisticBlock>> getPlacedBlocks(){
        return blockCache.getAllLogisticBlocks();
    }
    
    public Optional<LogisticKey> getKey(LogisticBlock block){
        return typeRegister.getKey(block);
    }
    
    public Optional<LogisticKey> getKey(Class<? extends LogisticBlock> block){
        return typeRegister.getKey(block);
    }

}
