package com.logisticscraft.logisticsapi.liquid;

import lombok.Getter;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;

@Getter
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

}
