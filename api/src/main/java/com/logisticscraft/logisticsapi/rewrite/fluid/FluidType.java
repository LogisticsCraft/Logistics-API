package com.logisticscraft.logisticsapi.rewrite.fluid;

import com.logisticscraft.logisticsapi.rewrite.data.LogisticKey;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Delegate;

@Data
public class FluidType {

    @NonNull
    @Delegate
    private final LogisticKey id;

}
