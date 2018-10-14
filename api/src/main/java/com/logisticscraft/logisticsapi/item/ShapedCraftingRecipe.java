package com.logisticscraft.logisticsapi.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice.MaterialChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;

import com.logisticscraft.logisticsapi.LogisticsApi;
import com.logisticscraft.logisticsapi.api.ItemManager;
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
    @Getter
    private String group;
    private Map<Character, Material> vanillaIngredients;
    private Map<Character, List<Material>> vanillaGroups;
    private Map<Character, ItemStack> ingredients;
    private String[] recipe;
    private String permission;

    private boolean registered = false;
    private ShapedRecipe bukkitRecipe;
    private final ItemManager manager = LogisticsApi.getInstance().getItemManager();

    @Builder
    public ShapedCraftingRecipe(@NonNull Plugin plugin, @NonNull String key, @NonNull ItemStack crafts, String group,
            @Singular("addVanillaIngredient") Map<Character, Material> vanillaIngredients, @Singular("addVanillaIngredient") Map<Character, List<Material>> vanillaGroups,
            @Singular("addIngredient") Map<Character, ItemStack> ingredients, @NonNull String[] recipe , String permission) {
        super();
        this.namespacedKey = new NamespacedKey(plugin, key);
        this.plugin = plugin;
        this.key = key;
        this.group = group;
        if(vanillaIngredients == null)vanillaIngredients = new HashMap<>();
        if(ingredients == null)ingredients = new HashMap<>();
        this.result = crafts;
        this.vanillaIngredients = vanillaIngredients;
        this.vanillaGroups = vanillaGroups;
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
        if(group != null)
            recipe.setGroup(group);
        for(Entry<Character, Material> s : vanillaIngredients.entrySet())
            recipe.setIngredient(s.getKey(), s.getValue());
        for(Entry<Character, ItemStack> s : ingredients.entrySet())
            recipe.setIngredient(s.getKey(), s.getValue().getData());
        for(Entry<Character, List<Material>> s : vanillaGroups.entrySet())
            recipe.setIngredient(s.getKey(), new MaterialChoice(s.getValue()));
        Bukkit.addRecipe(recipe);
        bukkitRecipe = recipe;
        Bukkit.getPluginManager().callEvent(new RecipeRegisterEvent(this, recipe));
    }

    @EventHandler(priority=EventPriority.LOW)
    public void preOnPrepareCraft(PrepareItemCraftEvent e) {
        org.bukkit.inventory.Recipe r = e.getInventory().getRecipe();
        if (r == null || e.getViewers().size() != 1) {
            return;
        }
        if(r instanceof ShapedRecipe && ((ShapedRecipe)r).getKey().equals(namespacedKey)){
            e.getInventory().setResult(null);
        }
    }

    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent e) {
        org.bukkit.inventory.Recipe r = e.getInventory().getRecipe();
        if (r == null || e.getViewers().size() != 1) {
            return;
        }

        if (r instanceof ShapedRecipe) {
            if(e.getInventory().getResult() != null && !e.getInventory().getResult().getType().equals(Material.AIR))return;
            String pattern = "";
            for(String s : recipe)pattern += s;
            char[] charPattern = pattern.toCharArray();
            int min = 0;
            for (int i = 0; i < charPattern.length; i++) {
                while(min < 10){
                    min++;
                    ItemStack is = e.getInventory().getItem(min);
                    if (is != null && vanillaIngredients.containsKey(charPattern[i]) && is.getType().equals(vanillaIngredients.get(charPattern[i]))) {
                        break;//Ok
                    }else if (is != null && vanillaGroups.containsKey(charPattern[i]) && vanillaGroups.get(charPattern[i]).contains(is.getType())) {
                        break;//Ok
                    }else if(is != null && ingredients.containsKey(charPattern[i])) {
                        if(is.isSimilar(ingredients.get(charPattern[i])))break;
                        Optional<LogisticItem> source = manager.getLogisticItem(is);
                        Optional<LogisticItem> target = manager.getLogisticItem(ingredients.get(charPattern[i]));
                        if(source.isPresent() && target.isPresent() && source.get().getKey().equals(target.get().getKey()))
                            break;//Ok
                    }else if(charPattern[i] == ' '){
                        break;//Ok
                    }
                }
                if(min == 10){
                    return;
                }
            }
            min++;
            for(int i = charPattern.length; i < 9; i++){ //Recipe matches so far, check the rest
                ItemStack is = e.getInventory().getItem(min);
                if(is != null && is.getType() != Material.AIR){
                    return;
                }
            }
            e.getInventory().setResult(result);
            e.getViewers().get(0).discoverRecipe(this.namespacedKey);
        }
    }

    public ItemStack getIngredient(char c){
        //Spigot changes the chars beginning from a
        HashMap<Character, ItemStack> spigotMapping = new HashMap<>();
        int tc = 'a';
        String pattern = "";
        for(String s : recipe)pattern += s;
        char[] charPattern = pattern.toCharArray();
        for(char p : charPattern){
            if(vanillaIngredients.containsKey(p)){
                spigotMapping.put((char)tc, new ItemStack(vanillaIngredients.get(p)));
                tc++;
            }else if(vanillaGroups.containsKey(p)){
                spigotMapping.put((char)tc, new ItemStack(vanillaGroups.get(p).get(0)));
                tc++;
            }else if(ingredients.containsKey(p)){
                spigotMapping.put((char)tc, ingredients.get(p));
                tc++;
            }else{
                spigotMapping.put((char)tc, null);
                tc++;
            }
        }
        if(spigotMapping.containsKey(c)){
            return spigotMapping.get(c);
        }
        return null;
    }

    @Override
    public org.bukkit.inventory.Recipe getBukkitRecipe() {
        return bukkitRecipe;
    }

}
