package com.logisticsapi.liquid;

import java.util.Map;

public interface FluidContainer {

    public Map<Fluid, Long> getFluidContent();
    
    public Long getAmount(Fluid fluid);
    
    public Long getSpace(Fluid fluid);
    
    public Long insertFluid(Fluid fluid, Long amount);
    
    public Long extractFluid(Fluid fluid, Long amount);
    
}
