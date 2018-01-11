package com.logisticscraft.logisticsapi.command;

import javax.inject.Inject;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import com.logisticscraft.logisticsapi.item.LogisticItem;
import com.logisticscraft.logisticsapi.item.LogisticItemRegister;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;

@CommandAlias("lapi")
public class DebugCommands extends BaseCommand implements InventoryHolder {

    @Inject
    private LogisticItemRegister itemRegister;

    @Subcommand("cheat|creative")
    public void onCheat(Player player) { // Just testing really
        Inventory inv = Bukkit.createInventory(this, 6 * 9);
        int i = 0;
        for (LogisticItem item : itemRegister.getRegisteredItems().values()) {
            inv.setItem(i++, item.getItemStack(1));
        }
        player.openInventory(inv);
    }

    @Override
    public Inventory getInventory() {
        return null;
    }

}
