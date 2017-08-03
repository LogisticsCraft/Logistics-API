package com.logisticsapi.liquid;

import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;

public class Fluid {

    private String internalName;
    private String parentPlugin;
    private String displayName;
    private MaterialData material;
    private float density;
    private boolean gaseous;
    private int temperature;

    public Fluid(Plugin plugin, String internalName, String displayName, MaterialData material,
                 float density, boolean gaseous, int temperature) {
        this.internalName = internalName;
        this.displayName = displayName;
        this.parentPlugin = plugin.getName();
        this.material = material;
        this.density = density;
        this.gaseous = gaseous;
        this.temperature = temperature;
    }

    public String getInternalName() {
        return internalName;
    }

    public String getParentPlugin() {
        return parentPlugin;
    }

    public String getDisplayName() {
        return displayName;
    }

    public MaterialData getMaterial() {
        return material;
    }

    public float getDensity() {
        return density;
    }

    public boolean isGaseous() {
        return gaseous;
    }

    public int getTemperature() {
        return temperature;
    }

}