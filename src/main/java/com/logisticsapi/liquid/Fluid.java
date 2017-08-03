package com.logisticsapi.liquid;

import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;

public class Fluid {

    private String internalname;
    private String parentplugin;
    private String displayname;
    private MaterialData material;
    private float density;
    private boolean gaseous;
    private int temperature;
    
    public Fluid(Plugin plugin, String internalname, String displayname, MaterialData material, float density, boolean gaseous,
            int temperature) {
        this.internalname = internalname;
        this.displayname = displayname;
        this.parentplugin = plugin.getName();
        this.material = material;
        this.density = density;
        this.gaseous = gaseous;
        this.temperature = temperature;
    }

    public String getInternalname() {
        return internalname;
    }

    public String getParentplugin() {
        return parentplugin;
    }

    public String getDisplayname() {
        return displayname;
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
