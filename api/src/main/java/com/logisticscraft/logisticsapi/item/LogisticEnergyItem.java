package com.logisticscraft.logisticsapi.item;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import de.tr7zw.itemnbtapi.NBTItem;
import lombok.NonNull;

public abstract class LogisticEnergyItem extends LogisticItem{

    public LogisticEnergyItem(Plugin plugin, String name, ItemStack baseStack) {
        super(plugin, name, baseStack);
    }
    
    public abstract long getMaxPower();

    public long getPower(@NonNull ItemStack item){
        NBTItem nbti = new NBTItem(item);
        if(nbti.hasKey("energy")){
            return nbti.getLong("energy");
        }
        return 0;
    }
    
    public ItemStack setPower(@NonNull ItemStack item, long power){
        if(power > getMaxPower())power = getMaxPower();
        if(power < 0)power = 0;
        NBTItem nbti = new NBTItem(item);
        nbti.setLong("energy", power);
        return nbti.getItem();
    }

}
