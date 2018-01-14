package com.logisticscraft.logisticsapi.item;

import java.util.HashMap;

import org.bukkit.NamespacedKey;

import com.logisticscraft.logisticsapi.utils.Tracer;

public class CraftingManager {

    private HashMap<NamespacedKey, Recipe> recipes = new HashMap<>();
    
    public void addRecipe(Recipe recipe){
        if(recipes.containsKey(recipe.getNamespacedKey())){
            Tracer.warn("Recipe-Key already in use: " + recipe.getNamespacedKey());
            return;
        }
        recipes.put(recipe.getNamespacedKey(), recipe);
    }
    
}
