package com.logisticscraft.logisticsapi.item;

import com.logisticscraft.logisticsapi.utils.Tracer;
import org.bukkit.NamespacedKey;

import java.util.HashMap;

public class CraftingManager {

    private HashMap<NamespacedKey, Recipe> recipes = new HashMap<>();

    public void addRecipe(Recipe recipe) {
        if (recipes.containsKey(recipe.getNamespacedKey())) {
            Tracer.warn("Recipe-Key already in use: " + recipe.getNamespacedKey());
            return;
        }
        recipes.put(recipe.getNamespacedKey(), recipe);
    }
}
