package com.logisticscraft.logisticsapi.fluid;

import com.logisticscraft.logisticsapi.data.LogisticKey;
import lombok.Data;
import lombok.NonNull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

@Data
public class LogisticFluid {

    @NonNull
    private final LogisticKey id;
    @NonNull
    private Plugin parentPlugin;
    @NonNull
    private String fluidDictionary;
    @NonNull
    private String displayName;
    private ItemStack representingItem;
    private float density;
    private boolean gaseous;
    private int temperature;

    public LogisticFluid(@NonNull Plugin plugin, @NonNull String internalName, @NonNull String displayName,
                         @NonNull String fluidDictionary, @NonNull ItemStack representingItem, float density, boolean gaseous,
                         int temperature) {
        this.id = new LogisticKey(plugin, internalName);
        this.displayName = displayName;
        this.parentPlugin = plugin;
        this.representingItem = representingItem;
        this.fluidDictionary = fluidDictionary;
        this.density = density;
        this.gaseous = gaseous;
        this.temperature = temperature;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof LogisticFluid))
            return false;
        LogisticFluid fluid = (LogisticFluid) object;
        if (getId().equals(fluid.getId()))
            return true;
        if (getFluidDictionary().equals(fluid.getFluidDictionary()))
            return true;
        return false;
    }

    @Override
    public int hashCode() {
        return id.toString().hashCode();
    }

}
