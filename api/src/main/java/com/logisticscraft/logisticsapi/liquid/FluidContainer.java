package com.logisticscraft.logisticsapi.liquid;

import java.util.Map;

public interface FluidContainer {

    Map<Fluid, Long> getFluidContent();

    long getAmount(Fluid fluid);

    long getSpace(Fluid fluid);

    long insertFluid(Fluid fluid, Long amount);

    long extractFluid(Fluid fluid, Long amount);
    
}
