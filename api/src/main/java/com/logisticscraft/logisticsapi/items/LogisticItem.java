package com.logisticscraft.logisticsapi.items;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.logisticscraft.logisticsapi.data.LogisticKey;

import de.tr7zw.itemnbtapi.NBTItem;
import lombok.Getter;

public class LogisticItem {

	@Getter
	private LogisticKey key;
	@Getter
	private Plugin plugin;
	private ItemStack itemStack;
	private NBTItem nbtItem;
	
	public LogisticItem(Plugin plugin, String name, ItemStack baseStack){
		key = new LogisticKey(plugin, name);
		itemStack = baseStack.clone();
		nbtItem = new NBTItem(itemStack);
		nbtItem.setString("itemid", key.toString());
	}
	
	public ItemStack getItemStack(int amount){
		ItemStack tmp = nbtItem.getItem().clone();
		tmp.setAmount(amount);
		return tmp;
	}
	
    public void setItemDictionary(String type) {
        nbtItem.setString("itemdictionary", type);
    }

    public String getItemDictionary() {
        return nbtItem.getString("itemdictionary");
    }

    public boolean hasItemDictionary() {
        return getItemDictionary() != null;
    }
	
}
