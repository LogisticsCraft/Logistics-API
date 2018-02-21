package com.logisticscraft.logisticsapi.api;

import java.util.Optional;

import javax.inject.Inject;

import org.bukkit.inventory.ItemStack;

import com.logisticscraft.logisticsapi.data.LogisticKey;
import com.logisticscraft.logisticsapi.item.CraftingManager;
import com.logisticscraft.logisticsapi.item.LogisticItem;
import com.logisticscraft.logisticsapi.item.LogisticItemRegister;
import com.logisticscraft.logisticsapi.item.Recipe;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ItemManager {

    @Inject
    private LogisticItemRegister itemRegister;
    @Inject
    private CraftingManager craftingManager;

    public void registerLogisticItem(@NonNull LogisticItem logisticItem) {
        itemRegister.registerLogisticItem(logisticItem);
    }

    public Optional<LogisticItem> getLogisticItem(@NonNull LogisticKey logisticKey) {
        return itemRegister.getLogisticItem(logisticKey);
    }
    
    public void registerRecipe(@NonNull Recipe recipe){
        recipe.register();
        craftingManager.addRecipe(recipe);
    }
    
    public Optional<LogisticItem> getLogisticItem(@NonNull ItemStack item) {
        return itemRegister.getLogisticItem(item);
    }

}
