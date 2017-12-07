package com.logisticscraft.logisticsapi.block;

import org.bukkit.inventory.ItemStack;

import de.tr7zw.itemnbtapi.NBTCompound;
import de.tr7zw.itemnbtapi.NBTReflectionUtil;

public class NBTCache extends NBTCompound{

    private Object nbt;

    public NBTCache(){
        super(null, null);
        nbt = NBTReflectionUtil.getNewNBTTag();
    }

    protected Object getCompound() {
        return nbt;
    }

    protected void setCompound(Object tag) {
        nbt = tag;
    }

    public ItemStack getItem() {
        return null;
    }

    @Override
    protected void setItem(ItemStack item) {}

}
