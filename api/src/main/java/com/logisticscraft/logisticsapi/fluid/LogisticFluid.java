package com.logisticscraft.logisticsapi.fluid;

import com.logisticscraft.logisticsapi.data.LogisticKey;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Delegate;

import java.io.Serializable;

@Data
public class LogisticFluid {

    @NonNull
    private final LogisticKey id;

}
