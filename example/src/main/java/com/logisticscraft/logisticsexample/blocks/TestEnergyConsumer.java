package com.logisticscraft.logisticsexample.blocks;

import com.logisticscraft.logisticsapi.block.LogisticBlock;
import com.logisticscraft.logisticsapi.block.LogisticTickManager.Ticking;
import com.logisticscraft.logisticsapi.energy.EnergyInput;
import com.logisticscraft.logisticsapi.energy.EnergyStorage;
import org.bukkit.Material;

@EnergyStorage.EnergyStorageData(capacity = 10000)
@EnergyInput.EnergyInputData(maxReceive = 10)
public class TestEnergyConsumer extends LogisticBlock implements EnergyInput {

	// Running every 10 ticks
	@Ticking(ticks = 10)
	public void update() {
		getBlock().ifPresent(block -> {
			if (getStoredEnergy() == 0L) {
				block.setType(Material.WHITE_STAINED_GLASS);
				//block.setData((byte) new Random().nextInt(15));
			} else if (getStoredEnergy() > 0L) {
				block.setType(Material.GOLD_BLOCK);
			}
		});
		setStoredEnergy(Math.max(0L, getStoredEnergy() - 5L));
	}
}
