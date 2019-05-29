package com.logisticscraft.logisticsexample;

import com.logisticscraft.logisticsapi.LogisticsApi;
import com.logisticscraft.logisticsapi.api.BlockManager;
import com.logisticscraft.logisticsapi.api.ItemManager;
import com.logisticscraft.logisticsapi.block.SimpleLogisticBlockFactory;
import com.logisticscraft.logisticsapi.item.LogisticBlockItem;
import com.logisticscraft.logisticsapi.item.ShapedCraftingRecipe;
import com.logisticscraft.logisticsexample.blocks.TestBlock;
import com.logisticscraft.logisticsexample.blocks.TestEnergyConsumer;
import com.logisticscraft.logisticsexample.blocks.TestEnergyProducer;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class LogisticsExamplePlugin extends JavaPlugin implements Listener {

    private LogisticBlockItem testBlockItem;
    
    @Override
    public void onLoad() {
        BlockManager blockManager = LogisticsApi.getInstance().getBlockManager();
        blockManager.registerLogisticBlock(this, "testBlock", TestBlock.class, new SimpleLogisticBlockFactory(TestBlock::new));
        blockManager.registerLogisticBlock(this, "testEnergyConsumer", TestEnergyConsumer.class, new SimpleLogisticBlockFactory(TestEnergyConsumer::new));
        blockManager.registerLogisticBlock(this, "testEnergyProducer", TestEnergyProducer.class, new SimpleLogisticBlockFactory(TestEnergyProducer::new));

        ItemManager itemManager = LogisticsApi.getInstance().getItemManager();
        ItemStack item = new ItemStack(Material.STICK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("TestBlock");
        item.setItemMeta(meta);
        testBlockItem = new LogisticBlockItem(TestBlock.class, item);
        itemManager.registerLogisticItem(testBlockItem);
        
        item = new ItemStack(Material.BROWN_TERRACOTTA);
        meta = item.getItemMeta();
        meta.setDisplayName("TestEnergyProducer");
        item.setItemMeta(meta);
        itemManager.registerLogisticItem(new LogisticBlockItem(TestEnergyProducer.class, item));
        
        item = new ItemStack(Material.WHITE_STAINED_GLASS);
        meta = item.getItemMeta();
        meta.setDisplayName("TestEnergyConsumer");
        item.setItemMeta(meta);
        itemManager.registerLogisticItem(new LogisticBlockItem(TestEnergyConsumer.class, item));
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        ShapedCraftingRecipe.builder().plugin(this).key("testBlockCrafter").addVanillaIngredient('x', Material.DIRT).recipe(new String[]{"xx", "xx"})
        .crafts(testBlockItem.getItemStack(4)).build().register();
        ShapedCraftingRecipe.builder().plugin(this).key("uncraftTestBlock").addIngredient('x', testBlockItem.getItemStack(1)).recipe(new String[]{" x ", "x x", " x "})
        .crafts(new ItemStack(Material.DIRT, 4)).build().register();
    }
}
