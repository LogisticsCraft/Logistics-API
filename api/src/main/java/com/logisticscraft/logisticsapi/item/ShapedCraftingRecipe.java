package com.logisticscraft.logisticsapi.item;

import com.logisticscraft.logisticsapi.LogisticsApi;
import com.logisticscraft.logisticsapi.api.ItemManager;
import com.logisticscraft.logisticsapi.event.RecipeRegisterEvent;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// TODO: move logic to a manager class
public class ShapedCraftingRecipe implements Recipe, Listener {

    @Getter
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
    @Getter
    private String permission;

    private boolean registered = false;
    private ShapedRecipe bukkitRecipe;
    private final ItemManager manager = LogisticsApi.getInstance().getItemManager();

    @Builder
    public ShapedCraftingRecipe(@NonNull Plugin plugin, @NonNull String key, @NonNull ItemStack crafts, String group,
                                @Singular("addVanillaIngredient") Map<Character, Material> vanillaIngredients,
                                @Singular("addVanillaIngredient") Map<Character, List<Material>> vanillaGroups,
                                @Singular("addIngredient") Map<Character, ItemStack> ingredients,
                                @NonNull String[] recipe, String permission) {
        super();
        this.namespacedKey = new NamespacedKey(plugin, key);
        this.plugin = plugin;
        this.key = key;
        this.group = group;
        if (vanillaIngredients == null) {
            vanillaIngredients = new HashMap<>();
        }
        if (ingredients == null) {
            ingredients = new HashMap<>();
        }
        this.result = crafts;
        this.vanillaIngredients = vanillaIngredients;
        this.vanillaGroups = vanillaGroups;
        this.ingredients = ingredients;
        this.recipe = recipe;
        this.permission = permission;
    }

    @Override
    public void register() {
        if (registered) {
            // TODO: add Tracer warning
            return;
        }
        registered = true;
        LogisticsApi.getInstance().getItemManager().registerRecipe(this);
        Bukkit.getPluginManager().registerEvents(this, LogisticsApi.getInstance());
        ShapedRecipe recipe = new ShapedRecipe(namespacedKey, result);
        recipe.shape(this.recipe);
        if (group != null) {
            recipe.setGroup(group);
        }
        vanillaIngredients.forEach(recipe::setIngredient);
        ingredients.forEach((key, item) -> recipe.setIngredient(key, item.getData()));
        vanillaGroups.forEach((key, items) -> recipe.setIngredient(key, new MaterialChoice(items)));
        Bukkit.addRecipe(recipe);
        bukkitRecipe = recipe;
        Bukkit.getPluginManager().callEvent(new RecipeRegisterEvent(this, recipe));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void preOnPrepareCraft(PrepareItemCraftEvent event) {
        org.bukkit.inventory.Recipe recipe = event.getInventory().getRecipe();
        if (recipe == null || event.getViewers().size() != 1) {
            return;
        }
        if (recipe instanceof ShapedRecipe && ((ShapedRecipe) recipe).getKey().equals(namespacedKey)) {
            event.getInventory().setResult(null);
        }
    }

    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        org.bukkit.inventory.Recipe recipe = event.getInventory().getRecipe();
        if (recipe == null || event.getViewers().size() != 1) {
            return;
        }
        if (recipe instanceof ShapedRecipe) {
            if (event.getInventory().getResult() != null && !event.getInventory().getResult().getType().equals(Material.AIR)) {
                return;
            }
            char[] charPattern = String.join("", this.recipe).toCharArray();
            int minimum = 0;
            for (char currentChar : charPattern) {
                while (minimum < 10) {
                    minimum++;
                    ItemStack item = event.getInventory().getItem(minimum);
                    if (item != null && vanillaIngredients.containsKey(currentChar) && item.getType().equals(vanillaIngredients.get(currentChar))) {
                        break; // Ok
                    } else if (item != null && vanillaGroups.containsKey(currentChar) && vanillaGroups.get(currentChar).contains(item.getType())) {
                        break; // Ok
                    } else if (item != null && ingredients.containsKey(currentChar)) {
                        if (item.isSimilar(ingredients.get(currentChar))) {
                            break;
                        }
                        Optional<LogisticItem> source = manager.getLogisticItem(item);
                        Optional<LogisticItem> target = manager.getLogisticItem(ingredients.get(currentChar));
                        if (source.isPresent() && target.isPresent() && source.get().getKey().equals(target.get().getKey())) {
                            break; // Ok
                        }
                    } else if (currentChar == ' ') {
                        break; // Ok
                    }
                }
                if (minimum == 10) {
                    return;
                }
            }
            minimum++;
            for (int index = charPattern.length; index < 9; index++) { // Recipe matches so far, check the rest
                ItemStack item = event.getInventory().getItem(minimum);
                if (item != null && item.getType() != Material.AIR) {
                    return;
                }
            }
            event.getInventory().setResult(result);
            event.getViewers().get(0).discoverRecipe(this.namespacedKey);
        }
    }

    public ItemStack getIngredient(char character) {
        // Spigot changes the chars beginning from a
        HashMap<Character, ItemStack> spigotMapping = new HashMap<>();
        int targetCharacter = 'a';
        String pattern = String.join("", recipe);
        char[] charPattern = pattern.toCharArray();
        for (char currentChar : charPattern) {
            if (vanillaIngredients.containsKey(currentChar)) {
                spigotMapping.put((char) targetCharacter, new ItemStack(vanillaIngredients.get(currentChar)));
                targetCharacter++;
            } else if (vanillaGroups.containsKey(currentChar)) {
                spigotMapping.put((char) targetCharacter, new ItemStack(vanillaGroups.get(currentChar).get(0)));
                targetCharacter++;
            } else if (ingredients.containsKey(currentChar)) {
                spigotMapping.put((char) targetCharacter, ingredients.get(currentChar));
                targetCharacter++;
            } else {
                spigotMapping.put((char) targetCharacter, null);
                targetCharacter++;
            }
        }
        if (spigotMapping.containsKey(character)) {
            return spigotMapping.get(character);
        }
        return null;
    }

    @Override
    public org.bukkit.inventory.Recipe getBukkitRecipe() {
        return bukkitRecipe;
    }
}
