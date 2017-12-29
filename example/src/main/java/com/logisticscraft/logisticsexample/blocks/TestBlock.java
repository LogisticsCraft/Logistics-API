package com.logisticscraft.logisticsexample.blocks;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import com.logisticscraft.logisticsapi.block.LogisticBlock;
import com.logisticscraft.logisticsapi.block.LogisticTickManager;
import com.logisticscraft.logisticsapi.energy.EnergyInput;
import com.logisticscraft.logisticsapi.energy.EnergyOutput;
import com.logisticscraft.logisticsapi.energy.EnergyStorage;
import com.logisticscraft.logisticsapi.item.InventoryStorage;

@EnergyStorage.EnergyStorageData(capacity = 10000)
@EnergyInput.EnergyInputData(maxReceive = 100)
@EnergyOutput.EnergyOutputData(maxExtract = 100)
@InventoryStorage.InventoryData(rows = 1, name = "TestBlock")
public class TestBlock extends LogisticBlock implements EnergyInput, EnergyOutput, InventoryStorage, InventoryHolder {

    @LogisticTickManager.Ticking(ticks = 10)
    public void update() {
        // Running every 10 ticks
        Block block = getLocation().getBlock().get();
        block.setType(Material.WOOL);
        block.setData((byte) new Random().nextInt(15));
    }

    @Override
    public InventoryHolder getInventoryHolder() {
        return this;
    }

    @Override
    public Inventory getInventory() {
        return getStoredInventory();
    }

    @Override
    public void onPlayerBreak(BlockBreakEvent event) {
        event.getPlayer().sendMessage("BlockBreak");
    }

    @Override
    public void onLeftClick(PlayerInteractEvent event) {
        event.getPlayer().sendMessage("LeftClick");
    }

    @Override
    public void onRightClick(PlayerInteractEvent event) {
        event.getPlayer().sendMessage("RightClick");
    }  

}
