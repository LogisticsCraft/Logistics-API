package com.logisticscraft.logisticsapi.block.conduit;

import com.logisticscraft.logisticsapi.block.LogisticBlock;
import com.logisticscraft.logisticsapi.data.LogisticKey;
import com.logisticscraft.logisticsapi.data.SafeBlockLocation;
import lombok.NonNull;

public abstract class Conduit extends LogisticBlock {

    Conduit(@NonNull LogisticKey typeId, @NonNull String name, @NonNull SafeBlockLocation location) {
        super(typeId, name, location);
    }

}
