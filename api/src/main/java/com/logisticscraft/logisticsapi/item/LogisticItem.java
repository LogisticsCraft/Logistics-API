package com.logisticscraft.logisticsapi.item;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.logisticscraft.logisticsapi.data.LogisticKey;

import de.tr7zw.itemnbtapi.NBTItem;
import lombok.Getter;
import lombok.NonNull;

public class LogisticItem {

    @Getter
    private LogisticKey key;

    private ItemStack itemStack;
    private NBTItem nbtItem;

    public LogisticItem(@NonNull final Plugin plugin, @NonNull final String name, @NonNull final ItemStack baseStack) {
        key = new LogisticKey(plugin, name);
        itemStack = baseStack.clone();
        nbtItem = new NBTItem(itemStack);
        nbtItem.setString("itemid", key.toString());
    }

    public ItemStack getItemStack(int amount) {
        ItemStack tmp = nbtItem.getItem().clone();
        tmp.setAmount(amount);
        return tmp;
    }

    public void setItemDictionary(@NonNull final String type) {
        nbtItem.setString("itemdictionary", type);
    }

    public String getItemDictionary() {
        return nbtItem.getString("itemdictionary");
    }

    public boolean hasItemDictionary() {
        return getItemDictionary() != null;
    }

}
