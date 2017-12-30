package com.logisticscraft.logisticsexample;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.logisticscraft.logisticsapi.LogisticsApi;
import com.logisticscraft.logisticsapi.api.BlockManager;
import com.logisticscraft.logisticsapi.block.LogisticBlock;
import com.logisticscraft.logisticsapi.block.LogisticBlockFactory;
import com.logisticscraft.logisticsexample.blocks.TestBlock;

import de.tr7zw.itemnbtapi.NBTCompound;

public final class LogisticsExamplePlugin extends JavaPlugin implements Listener{

    @Override
    public void onLoad() {
        BlockManager blockManager = LogisticsApi.getInstance().getBlockManager();
        blockManager.registerLogisticBlock(this, "testBlock", TestBlock.class, new LogisticBlockFactory() {
            
            @Override
            public LogisticBlock onPlace(Player player, ItemStack item, Location location) {
                return new TestBlock();
            }
            
            @Override
            public LogisticBlock onLoad(NBTCompound nbtData) {
                return new TestBlock();
            }
        });
    }
    
    @Override
    public void onEnable() {
        /*try {
            if(LogisticsLoader.install()) {
                getLogger().info("The required library has been downloaded correctly!");
            }
        } catch (LogisticInstallException e) {
            e.printStackTrace();
            getLogger().severe("Unable to download the required library, disabling...");
            setEnabled(false);
        }*/
        Bukkit.getPluginManager().registerEvents(this, this);
    }
    
    @EventHandler
    public void onPlace(BlockPlaceEvent event){
        if(event.getItemInHand().getType() == Material.WOOL){
            LogisticsApi.getInstance().getBlockManager().placeLogisticBlock(event.getBlock().getLocation(), new TestBlock());
        }
    }

}
