package com.logisticscraft.logisticsapi.block;

import de.tr7zw.itemnbtapi.NBTCompound;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface BlockFactory {

    /**
     * Called when a new block is placed.
     *
     * @param player the player who placed the block
     * @param item the item used by the player
     * @param location the block location
     * @return the logistic block instance
     */
    LogisticBlock onPlace(Player player, @NonNull ItemStack item, @NonNull Location location);

    /**
     * Called when a placed block is loaded (when the chunk is loaded).
     *
     * @param nbtData the stored block data
     * @return the logistic block instance
     */
    LogisticBlock onLoad(@NonNull NBTCompound nbtData);

}
