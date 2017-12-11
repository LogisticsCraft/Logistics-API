package com.logisticscraft.logisticsapi.block;

@EnergyStorage.EnergyStorageData(capacity = 10000)
@EnergyInput.EnergyInputData(maxReceive = 100)
@EnergyOutput.EnergyOutputData(maxExtract = 100)
public class TestBlock extends LogisticBlock implements EnergyStorage, EnergyInput, EnergyOutput {
}
