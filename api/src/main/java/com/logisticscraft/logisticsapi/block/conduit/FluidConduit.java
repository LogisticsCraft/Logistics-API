package com.logisticscraft.logisticsapi.block.conduit;

import com.logisticscraft.logisticsapi.data.LogisticKey;
import com.logisticscraft.logisticsapi.data.SafeBlockLocation;
import com.logisticscraft.logisticsapi.fluid.FluidInput;
import com.logisticscraft.logisticsapi.fluid.FluidOutput;

public class FluidConduit extends Conduit implements FluidInput, FluidOutput {

    public FluidConduit(LogisticKey typeId, String name, SafeBlockLocation location) {
        super(typeId, name, location);
    }

}
