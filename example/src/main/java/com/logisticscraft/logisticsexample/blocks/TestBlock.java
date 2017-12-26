package com.logisticscraft.logisticsexample.blocks;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import com.logisticscraft.logisticsapi.block.LogisticBlock;
import com.logisticscraft.logisticsapi.block.LogisticTickManager;
import com.logisticscraft.logisticsapi.data.LogisticKey;
import com.logisticscraft.logisticsapi.data.SafeBlockLocation;
import com.logisticscraft.logisticsapi.energy.EnergyInput;
import com.logisticscraft.logisticsapi.energy.EnergyOutput;
import com.logisticscraft.logisticsapi.energy.EnergyStorage;
import com.logisticscraft.logisticsapi.item.InventoryStorage;

@EnergyStorage.EnergyStorageData(capacity = 10000)
@EnergyInput.EnergyInputData(maxReceive = 100)
@EnergyOutput.EnergyOutputData(maxExtract = 100)
@InventoryStorage.InventoryData(rows = 1, name = "TestBlock")
public class TestBlock extends LogisticBlock implements EnergyInput, EnergyOutput, InventoryStorage, InventoryHolder {

    public TestBlock(LogisticKey typeId, String name, SafeBlockLocation location) {
        super(typeId, name, location);
    }

    @LogisticTickManager.Ticking(ticks = 10)
    public void update() {
        //Running every 10 ticks
    }

    @Override
    public InventoryHolder getInventoryHolder() {
        return this;
    }

    @Override
    public Inventory getInventory() {
        return getStoredInventory();
    }

}
