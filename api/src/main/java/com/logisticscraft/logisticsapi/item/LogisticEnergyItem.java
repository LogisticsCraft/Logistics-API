package com.logisticscraft.logisticsapi.item;

import de.tr7zw.itemnbtapi.NBTItem;
import lombok.NonNull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public abstract class LogisticEnergyItem extends LogisticItem {

    public LogisticEnergyItem(Plugin plugin, String name, ItemStack baseStack) {
        super(plugin, name, baseStack);
    }

    public abstract long getMaxPower();

    public long getPower(@NonNull ItemStack item) {
        NBTItem nbt = new NBTItem(item);
        if (nbt.hasKey("energy")) {
            return nbt.getLong("energy");
        }
        return 0;
    }

    public ItemStack setPower(@NonNull ItemStack item, long power) {
        if (power > getMaxPower()) {
            power = getMaxPower();
        }
        if (power < 0) {
            power = 0;
        }
        NBTItem nbt = new NBTItem(item);
        nbt.setLong("energy", power);
        return nbt.getItem();
    }
}
