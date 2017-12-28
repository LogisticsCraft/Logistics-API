package com.logisticscraft.logisticsapi.block;

import de.tr7zw.itemnbtapi.NBTCompound;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface BlockFactory {

    LogisticBlock onPlace(@NonNull Player player, @NonNull ItemStack item, @NonNull Location location);

    LogisticBlock onLoad(@NonNull NBTCompound nbtData);

}
