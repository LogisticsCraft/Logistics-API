package com.logisticscraft.logisticsexample.blocks;

import com.logisticscraft.logisticsapi.rewrite.block.LogisticBlock;
import com.logisticscraft.logisticsapi.rewrite.energy.EnergyInput;
import com.logisticscraft.logisticsapi.rewrite.energy.EnergyOutput;
import com.logisticscraft.logisticsapi.rewrite.energy.EnergyStorage;

@EnergyStorage.EnergyStorageData(capacity = 10000)
@EnergyInput.EnergyInputData(maxReceive = 100)
@EnergyOutput.EnergyOutputData(maxExtract = 100)
public class TestBlock extends LogisticBlock implements EnergyInput, EnergyOutput {

}
