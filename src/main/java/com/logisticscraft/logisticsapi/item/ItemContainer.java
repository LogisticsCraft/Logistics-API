package com.logisticscraft.logisticsapi.item;

import org.bukkit.inventory.ItemStack;

import com.logisticscraft.logisticsapi.BlockSide;

public interface ItemContainer {

	public ItemStack extractItem(BlockSide extractDirection, int extractAmount);

	public ItemStack insertItem(BlockSide insertDirection, ItemStack insertion);

	public int howMuchSpaceForItemAsync(BlockSide insertDirection, ItemStack insertion);

	public boolean isInterfaceable(BlockSide face);

}