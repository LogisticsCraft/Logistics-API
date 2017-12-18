package com.logisticscraft.logisticsapi.fluid;

import com.logisticscraft.logisticsapi.data.LogisticKey;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Delegate;

@Data
public class FluidType {

    @NonNull
    @Delegate
    private final LogisticKey id;

}
