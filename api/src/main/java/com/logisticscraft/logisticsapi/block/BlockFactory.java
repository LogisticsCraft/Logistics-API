package com.logisticscraft.logisticsapi.block;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.itemnbtapi.NBTCompound;
import lombok.NonNull;

public interface BlockFactory {

	public LogisticBlock onPlace(@NonNull Player player, @NonNull ItemStack item, @NonNull Location location);
	
	public LogisticBlock onLoad(@NonNull NBTCompound nbtData);
	
}
