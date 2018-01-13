package com.logisticscraft.logisticsexample;

import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.logisticscraft.logisticsapi.LogisticsApi;
import com.logisticscraft.logisticsapi.api.BlockManager;
import com.logisticscraft.logisticsapi.api.ItemManager;
import com.logisticscraft.logisticsapi.block.BasicBlockFactory;
import com.logisticscraft.logisticsapi.item.LogisticBlockItem;
import com.logisticscraft.logisticsexample.blocks.TestBlock;

public final class LogisticsExamplePlugin extends JavaPlugin implements Listener {

    @Override
    public void onLoad() {
        /*try {
            if (LogisticsLoader.install()) {
                getLogger().info("The required library has been downloaded correctly!");
            }
        } catch (LogisticInstallException e) {
            e.printStackTrace();
            getLogger().severe("Unable to download the required library, disabling...");
            setEnabled(false);
            return;
        }*/

        BlockManager blockManager = LogisticsApi.getInstance().getBlockManager();
        blockManager.registerLogisticBlock(this, "testBlock", TestBlock.class, new BasicBlockFactory(TestBlock.class));
        
        ItemManager itemManager = LogisticsApi.getInstance().getItemManager();
        ItemStack item = new ItemStack(Material.WOOL);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("TestBlock");
        item.setItemMeta(meta);
        itemManager.registerLogisticItem(new LogisticBlockItem(TestBlock.class, item));
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

}
