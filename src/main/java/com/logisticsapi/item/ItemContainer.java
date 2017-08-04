package com.logisticsapi.item;

import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

public interface ItemContainer {

    public ItemStack extractItem(BlockFace extractDirection);

    public boolean insertItem(BlockFace insertDirection, ItemStack insertion);

    public boolean isSpaceForItemAsync(BlockFace insertDirection, ItemStack insertion);
    
    public boolean isInterfaceable(BlockFace face);

}