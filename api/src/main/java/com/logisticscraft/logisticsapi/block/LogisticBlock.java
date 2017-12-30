package com.logisticscraft.logisticsapi.block;

import com.logisticscraft.logisticsapi.data.LogisticKey;
import com.logisticscraft.logisticsapi.data.SafeBlockLocation;
import com.logisticscraft.logisticsapi.data.holder.DataHolder;
import com.logisticscraft.logisticsapi.data.holder.PersistentDataHolder;
import com.logisticscraft.logisticsapi.data.holder.VolatileDataHolder;
import com.logisticscraft.logisticsapi.persistence.Persistent;
import lombok.Getter;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Optional;

/**
 * Represents a custom block handled by the API.
 */
public abstract class LogisticBlock implements PersistentDataHolder, VolatileDataHolder {

    @Getter
    @Persistent
    private LogisticKey typeId;

    @Getter
    @Persistent
    private SafeBlockLocation location;

    @Persistent
    private DataHolder persistentData = new DataHolder();
    private DataHolder volatileData = new DataHolder();

    /**
     * Returns the block object instance.
     *
     * @return the Block
     */
    public Optional<Block> getBlock() {
        return location.getBlock();
    }

    @Override
    public DataHolder getPersistentData() {
        return persistentData;
    }

    @Override
    public DataHolder getVolatileData() {
        return volatileData;
    }

    /**
     * Overwrite this method intercept the BlockBreakEvent.
     * If the event isn't cancelled the LogisticBlock will be removed.
     *
     * @param event the BlockBreakEvent
     */
    public void onPlayerBreak(BlockBreakEvent event) {
    }

    /**
     * Overwrite this method intercept the PlayerInteractEvent when the block is left-clicked.
     *
     * @param event the PlayerInteractEvent
     */
    public void onLeftClick(PlayerInteractEvent event) {
    }

    /**
     * Overwrite this method intercept the PlayerInteractEvent when the block is right-clicked.
     *
     * @param event the PlayerInteractEvent
     */
    public void onRightClick(PlayerInteractEvent event) {
    }

}
