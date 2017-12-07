package com.logisticscraft.logisticsapi.item;

import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.logisticscraft.logisticsapi.util.bukkit.BlockSide;

public interface ItemContainer {

	ItemStack extractItem(BlockSide extractDirection, int extractAmount, List<ItemStack> filterItems, FilteringMode filteringMode);

	ItemStack insertItem(BlockSide insertDirection, ItemStack insertion);

	int howMuchSpaceForItemAsync(BlockSide insertDirection, ItemStack insertion);

	boolean isInterfaceable(BlockSide face);

}