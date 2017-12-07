package com.logisticscraft.logisticsapi.block;

import com.logisticscraft.logisticsapi.block.EnergyStorage.EnergyStorageData;

@EnergyStorageData(capacity = 10000, maxExtract = 100, maxReceive = 100)
public class TestBlock extends LogisticBlock implements EnergyStorage{


}
