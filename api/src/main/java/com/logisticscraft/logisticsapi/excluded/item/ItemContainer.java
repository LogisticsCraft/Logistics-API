package com.logisticscraft.logisticsapi.excluded.item;

import com.logisticscraft.logisticsapi.excluded.util.bukkit.BlockSide;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface ItemContainer {

    ItemStack extractItem(BlockSide extractDirection, int extractAmount, List<ItemStack> filterItems, FilteringMode filteringMode);

    ItemStack insertItem(BlockSide insertDirection, ItemStack insertion);

    int howMuchSpaceForItemAsync(BlockSide insertDirection, ItemStack insertion);

    boolean isInterfaceable(BlockSide face);

}