package com.logisticscraft.logisticsapi.block;

import com.logisticscraft.logisticsapi.block.energy.EnergyInput;
import com.logisticscraft.logisticsapi.block.energy.EnergyOutput;
import com.logisticscraft.logisticsapi.block.energy.EnergyStorage;

@EnergyStorage.EnergyStorageData(capacity = 10000)
@EnergyInput.EnergyInputData(maxReceive = 100)
@EnergyOutput.EnergyOutputData(maxExtract = 100)
public class TestBlock extends LogisticBlock implements EnergyInput, EnergyOutput {
}
