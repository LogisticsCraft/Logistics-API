package com.logisticscraft.logisticsapi.block.conduit;

import com.logisticscraft.logisticsapi.data.LogisticKey;
import com.logisticscraft.logisticsapi.data.SafeBlockLocation;
import com.logisticscraft.logisticsapi.fluid.FluidInput;
import com.logisticscraft.logisticsapi.fluid.FluidOutput;
import lombok.NonNull;

public class FluidConduit extends Conduit implements FluidInput, FluidOutput {

    public FluidConduit(@NonNull LogisticKey typeId, @NonNull String name, @NonNull SafeBlockLocation location) {
        super(typeId, name, location);
    }

}
