package com.logisticscraft.logisticsexample.blocks;

import com.logisticscraft.logisticsapi.block.LogisticBlock;
import com.logisticscraft.logisticsapi.block.LogisticTickManager;
import com.logisticscraft.logisticsapi.energy.EnergyOutput;
import com.logisticscraft.logisticsapi.energy.EnergyStorage;
import org.bukkit.Material;
import java.util.Random;

@EnergyStorage.EnergyStorageData(capacity = 10000)
@EnergyOutput.EnergyOutputData(maxExtract = 100)
public class TestEnergyProducer extends LogisticBlock implements EnergyOutput{

    // Running every 10 ticks
    @LogisticTickManager.Ticking(ticks = 10)
    public void update() {
        getBlock().ifPresent(block -> {
            block.setType(Material.STAINED_CLAY);
            block.setData((byte) new Random().nextInt(15));
        });
        setStoredEnergy(getMaxEnergyStored());
    }

}
