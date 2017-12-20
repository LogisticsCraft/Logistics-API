package com.logisticscraft.logisticsapi.block.conduit;

import com.logisticscraft.logisticsapi.block.LogisticBlock;
import com.logisticscraft.logisticsapi.data.LogisticKey;
import com.logisticscraft.logisticsapi.data.SafeBlockLocation;

public abstract class Conduit extends LogisticBlock {

    public Conduit(LogisticKey typeId, String name, SafeBlockLocation location) {
        super(typeId, name, location);
    }
}
