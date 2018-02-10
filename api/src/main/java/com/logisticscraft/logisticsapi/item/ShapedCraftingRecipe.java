package com.logisticscraft.logisticsapi.item;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;

import com.logisticscraft.logisticsapi.LogisticsApi;
import com.logisticscraft.logisticsapi.event.RecipeRegisterEvent;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;


public class ShapedCraftingRecipe implements Recipe, Listener {

    private NamespacedKey namespacedKey;
    @Getter
    private Plugin plugin;
    @Getter
    private String key;
    @Getter
    private ItemStack result;
    private Map<Character, Material> vanillaIngredients;
    private Map<Character, ItemStack> ingredients;
    private String[] recipe;
    private String permission;
    
    private boolean registered = false;

    @Builder
    public ShapedCraftingRecipe(@NonNull Plugin plugin, @NonNull String key, @NonNull ItemStack crafts, 
            @Singular("addVanillaIngredient") Map<Character, Material> vanillaIngredients,
            @Singular("addIngredient") Map<Character, ItemStack> ingredients, @NonNull String[] recipe , String permission) {
        super();
        this.namespacedKey = new NamespacedKey(plugin, key);
        this.plugin = plugin;
        this.key = key;
        if(vanillaIngredients == null)vanillaIngredients = new HashMap<>();
        if(ingredients == null)ingredients = new HashMap<>();
        this.result = crafts;
        this.vanillaIngredients = vanillaIngredients;
        this.ingredients = ingredients;
        this.recipe = recipe;
        this.permission = permission;
    }
    
    @Override
    public NamespacedKey getNamespacedKey() {
        return namespacedKey;
    }

    @Override
    public void register() {
        if(registered)return;
        registered = true;
        LogisticsApi.getInstance().getItemManager().registerRecipe(this);
        Bukkit.getPluginManager().registerEvents(this, LogisticsApi.getInstance());
        ShapedRecipe recipe = new ShapedRecipe(namespacedKey, result);
        recipe.shape(this.recipe);
        for(Entry<Character, Material> s : vanillaIngredients.entrySet())
            recipe.setIngredient(s.getKey(), s.getValue());
        for(Entry<Character, ItemStack> s : ingredients.entrySet())
            recipe.setIngredient(s.getKey(), s.getValue().getData());
        Bukkit.addRecipe(recipe);
        Bukkit.getPluginManager().callEvent(new RecipeRegisterEvent(this, recipe));
    }

    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent e) {
        org.bukkit.inventory.Recipe r = e.getInventory().getRecipe();
        if (r == null || e.getViewers().size() != 1) {
            return;
        }

        Player viewer = (Player) e.getViewers().get(0);
        if (r instanceof ShapedRecipe && ((ShapedRecipe)r).getKey().equals(namespacedKey)) {
            if (permission != null) {
                if (!viewer.hasPermission(permission)) {
                    e.getInventory().setResult(null);
                    return;
                }
            }

            String pattern = "";
            for(String s : recipe)pattern += s;
            char[] charPattern = pattern.toCharArray();

            int min = 1;
            boolean prevent = false;
            for (int i = 0; i < charPattern.length; i++) {
                while(min < 10){
                    ItemStack is = e.getInventory().getItem(min);
                    if (is != null && vanillaIngredients.containsKey(charPattern[i]) && is.getType() == vanillaIngredients.get(charPattern[i])) {
                        break;//Ok
                    }else if(is != null && ingredients.containsKey(charPattern[i]) && is.isSimilar(ingredients.get(charPattern[i]))) {
                        break;//Ok
                    }else if(charPattern[i] == ' '){
                        break;//Ok
                    }
                    min++;
                }
                if(min == 10){
                    prevent = true;
                    break;
                }
            }
            if (prevent) {
                e.getInventory().setResult(null);
            }
        }
    }
    
    public ItemStack getIngredient(char c){
        if(ingredients.containsKey(c)){
            return ingredients.get(c);
        }
        if(vanillaIngredients.containsKey(c)){
            return new ItemStack(vanillaIngredients.get(c));
        }
        return null;
    }

}
