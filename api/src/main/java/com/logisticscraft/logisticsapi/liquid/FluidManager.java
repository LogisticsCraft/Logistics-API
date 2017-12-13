package com.logisticscraft.logisticsapi.liquid;


import com.logisticscraft.logisticsapi.LogisticsApiPlugin;
import com.logisticscraft.logisticsapi.rewrite.utils.Tracer;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

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

    public static Fluid registerFluid(@NonNull Fluid fluid) {
        Fluid re = fluids.putIfAbsent(fluid.getInternalName(), fluid);
        if(re != null){
            Tracer.info(fluid.getParentPlugin() + "'s '" + fluid.getInternalName() + "' fluid didn't get registered because of " + re.getParentPlugin() + "'s implementation!");
            return re;
        }else{
            Tracer.info(fluid.getParentPlugin() + " registered the fluid '" + fluid.getInternalName() + "'");
            return fluid;
        }
    }

    public static Fluid getFluid(String internalname){
        return fluids.get(internalname);
    }

    public static void registerFluidContainer(@NonNull final Location location,
            @NonNull final FluidContainer fluidContainer) {
        if (fluidContainers.putIfAbsent(location, fluidContainer) == null) Tracer.info(
                "FluidContainer registered at " + location.toString()
                );
        else Tracer.warn("Trying to register FluidContainer at occupied location: " + location.toString());
    }

    public static void unregisterFluidContainer(@NonNull final Location location) {
        if (fluidContainers.remove(location) == null) Tracer.warn("Attempt to unregister unknown FluidContainer");
    }

    public static boolean isContainerAt(@NonNull final Location location) {
        return fluidContainers.containsKey(location);
    }

    public static boolean isContainerRegistered(@NonNull final FluidContainer container) {
        return fluidContainers.containsValue(container);
    }

    public static FluidContainer getContainerAt(@NonNull final Location location) {
        return fluidContainers.get(location);
    }

    public static Location getContainerLocation(@NonNull final FluidContainer fluidContainer) {
        for (Map.Entry<Location, FluidContainer> entry : fluidContainers.entrySet()) if (entry.getValue()
                == fluidContainer) return entry.getKey();
        return null;
    }

}
