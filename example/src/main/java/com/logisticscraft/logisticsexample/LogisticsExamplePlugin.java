package com.logisticscraft.logisticsexample;

import com.logisticscraft.logisticsapi.LogisticsApi;
import com.logisticscraft.logisticsapi.api.BlockManager;
import com.logisticscraft.logisticsapi.api.ItemManager;
import com.logisticscraft.logisticsapi.block.LogisticBlock;
import com.logisticscraft.logisticsapi.block.LogisticBlockFactory;
import com.logisticscraft.logisticsapi.item.LogisticItem;
import com.logisticscraft.logisticsexample.blocks.TestBlock;
import com.logisticscraft.logisticsloader.LogisticInstallException;
import com.logisticscraft.logisticsloader.LogisticsLoader;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

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
        blockManager.registerLogisticBlock(this, "testBlock", TestBlock.class, new LogisticBlockFactory() {

            @Override
            public LogisticBlock onPlace(Player player, ItemStack item, Location location) {
                return new TestBlock();
            }

            @Override
            public LogisticBlock onLoad() {
                return new TestBlock();
            }
        });
        
        ItemManager itemManager = LogisticsApi.getInstance().getItemManager();
        ItemStack item = new ItemStack(Material.WOOL);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("TestBlock");
        item.setItemMeta(meta);
        LogisticItem logisticItem = new LogisticItem(this, "TestBlock", item);
        itemManager.registerLogisticItem(logisticItem);
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (event.getItemInHand().getType() == Material.WOOL) {
            LogisticsApi.getInstance().getBlockManager().placeLogisticBlock(event.getBlock().getLocation(), new TestBlock());
        }
    }

}
