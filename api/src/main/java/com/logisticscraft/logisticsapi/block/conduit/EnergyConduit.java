package com.logisticscraft.logisticsapi.block.conduit;

import com.logisticscraft.logisticsapi.data.LogisticKey;
import com.logisticscraft.logisticsapi.data.SafeBlockLocation;
import com.logisticscraft.logisticsapi.energy.EnergyInput;
import com.logisticscraft.logisticsapi.energy.EnergyOutput;

public class EnergyConduit extends Conduit implements EnergyInput, EnergyOutput {

    public EnergyConduit(LogisticKey typeId, String name, SafeBlockLocation location) {
        super(typeId, name, location);
    }
}
