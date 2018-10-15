package com.logisticscraft.logisticsapi.event;

import com.logisticscraft.logisticsapi.item.Recipe;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RecipeRegisterEvent extends Event {

    @Getter
    private static final HandlerList handlerList = new HandlerList();
    @Getter
    private Recipe recipe;
    @Getter
    private org.bukkit.inventory.Recipe vanillaRecipe;

    public RecipeRegisterEvent(@NonNull Recipe recipe, org.bukkit.inventory.Recipe vanillaRecipe) {
        this.recipe = recipe;
        this.vanillaRecipe = vanillaRecipe;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }


}
