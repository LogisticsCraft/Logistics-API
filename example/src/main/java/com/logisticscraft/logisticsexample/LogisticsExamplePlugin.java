package com.logisticscraft.logisticsexample;

import com.logisticscraft.logisticsapi.block.BlockFactory;
import com.logisticscraft.logisticsapi.block.LogisticBlock;
import com.logisticscraft.logisticsexample.blocks.TestBlock;
import com.logisticscraft.logisticsloader.LogisticInstallException;
import com.logisticscraft.logisticsloader.LogisticsLoader;

import de.tr7zw.itemnbtapi.NBTCompound;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class LogisticsExamplePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        try {
            if(LogisticsLoader.install()) {
                getLogger().info("The required library has been downloaded correctly!");
            }
        } catch (LogisticInstallException e) {
            e.printStackTrace();
            getLogger().severe("Unable to download the required library, disabling...");
            setEnabled(false);
        }
        BlockFactory factory = new BlockFactory() {
			
			@Override
			public LogisticBlock onPlace(Player player, ItemStack item, Location location) {
				return new TestBlock();
			}
			
			@Override
			public LogisticBlock onLoad(NBTCompound nbtData) {
				return new TestBlock();
			}
		};
    }

}
