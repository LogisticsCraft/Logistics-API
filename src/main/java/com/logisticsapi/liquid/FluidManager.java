package com.logisticsapi.liquid;


import com.logisticsapi.LogisticsApiPlugin;
import com.logisticsapi.util.console.Tracer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class FluidManager {

    private static Map<String, Fluid> fluids = new HashMap<>();
    private static Map<Location, FluidContainer> fluidContainers = new HashMap<>();

    public static final Fluid WATER = registerFluid(
            new Fluid(LogisticsApiPlugin.getInstance(), "water", "Water",
                    new MaterialData(Material.LAPIS_BLOCK), 1000, false, 15)
    );
    public static final Fluid LAVA = registerFluid(
            new Fluid(LogisticsApiPlugin.getInstance(), "lava", "Lava",
                    new MaterialData(Material.MAGMA), 2450, false, 900)
    );

    public static Fluid registerFluid(@Nonnull Fluid fluid) {
        Fluid re = fluids.putIfAbsent(fluid.getInternalName(), fluid);
        if(re != null){
            Tracer.msg(fluid.getParentPlugin() + "'s '" + fluid.getInternalName() + "' fluid didn't get registered because of " + re.getParentPlugin() + "'s implementation!");
            return re;
        }else{
            Tracer.msg(fluid.getParentPlugin() + " registered the fluid '" + fluid.getInternalName() + "'");
            return fluid;
        }
    }

    @Nullable
    public static Fluid getFluid(String internalname){
        return fluids.get(internalname);
    }

    public static void registerFluidContainer(@Nonnull final Location location,
            @Nonnull final FluidContainer fluidContainer) {
        if (fluidContainers.putIfAbsent(location, fluidContainer) == null) Tracer.msg(
                "Fluid Container registered at " + location.toString()
                );
        else Tracer.msg("Fluid Container re-registered at " + location.toString());
    }

    public static void unregisterFluidContainer(@Nonnull final Location location) {
        if (fluidContainers.remove(location) == null) Tracer.warn("Attempt to unregister unknown FluidContainer");
    }

    public static boolean isContainerAt(@Nonnull final Location location) {
        return fluidContainers.containsKey(location);
    }

    public static boolean isContainerRegistered(@Nonnull final FluidContainer container) {
        return fluidContainers.containsValue(container);
    }

    @Nullable
    public static FluidContainer getContainerAt(@Nonnull final Location location) {
        return fluidContainers.get(location);
    }

    @Nullable
    public static Location getContainerLocation(@Nonnull final FluidContainer fluidContainer) {
        for (Map.Entry<Location, FluidContainer> entry : fluidContainers.entrySet()) if (entry.getValue()
                == fluidContainer) return entry.getKey();
        return null;
    }

}
