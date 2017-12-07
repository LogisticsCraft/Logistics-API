package com.logisticscraft.logisticsapi.item;

import java.util.Collection;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.logisticscraft.logisticsapi.util.bukkit.BlockSide;

public interface InventoryContainer extends ItemContainer {

	Inventory getInventory();

	static ItemStack changeAmount(ItemStack item, int amountDelta) {
		ItemStack copy = item.clone();
		if (item.getAmount() + amountDelta > 0) {
			copy.setAmount(Math.min(item.getMaxStackSize(), item.getAmount() + amountDelta));
		} else {
			copy = null;
		}
		return copy;
	}

	@Override
	default ItemStack extractItem(BlockSide extractDirection, int extractAmount, List<ItemStack> filterItems, FilteringMode filteringMode) {
		Inventory cachedInv = getInventory();
		ItemStack takenIs = null;
		for (int i = 0; i < cachedInv.getSize(); i++) {
			if (cachedInv.getItem(i) != null) {
				int amountBefore = takenIs != null ? takenIs.getAmount() : 0;
				if (takenIs == null) {
					if (new ItemData(cachedInv.getItem(i)).checkFilter(filterItems, filteringMode)) {
						takenIs = cachedInv.getItem(i).clone();
						takenIs.setAmount(Math.min(extractAmount, takenIs.getAmount()));
					} else {
						continue;
					}
				} else if (takenIs.isSimilar(cachedInv.getItem(i))) {
					takenIs.setAmount(Math.min(extractAmount, amountBefore + cachedInv.getItem(i).getAmount()));
				}
				ItemStack invItem = cachedInv.getItem(i);
				cachedInv.setItem(i, changeAmount(invItem, -(takenIs.getAmount() - amountBefore)));
			}
		}
		return takenIs;
	}

	@Override
	default ItemStack insertItem(BlockSide insertDirection, ItemStack insertion) {
		Inventory cachedInv = getInventory();
		Collection<ItemStack> overflow = cachedInv.addItem(insertion).values();
		if (overflow.isEmpty()) {
			return null;
		} else {
			return overflow.toArray(new ItemStack[0])[0];
		}
	}

	@Override
	default int howMuchSpaceForItemAsync(BlockSide insertDirection, ItemStack insertion) {
		Inventory cachedInv = getInventory();
		int freeSpace = 0;
		for (int i = 0; i < cachedInv.getSize(); i++) {
			ItemStack is = cachedInv.getItem(i);
			if (is == null || is.getType() == Material.AIR) {
				freeSpace += insertion.getMaxStackSize();
			} else if (is.isSimilar(insertion) && is.getAmount() < is.getMaxStackSize()) {
				freeSpace += is.getMaxStackSize() - is.getAmount();
			}
		}
		return freeSpace;
	}

	@Override
	default boolean isInterfaceable(BlockSide face) {
		return true;
	}

}
