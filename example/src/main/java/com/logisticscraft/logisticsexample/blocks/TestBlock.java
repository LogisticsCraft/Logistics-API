package com.logisticscraft.logisticsexample.blocks;

import com.logisticscraft.logisticsapi.block.LogisticBlock;
import com.logisticscraft.logisticsapi.block.LogisticTickManager;
import com.logisticscraft.logisticsapi.energy.EnergyInput;
import com.logisticscraft.logisticsapi.energy.EnergyOutput;
import com.logisticscraft.logisticsapi.energy.EnergyStorage;
import com.logisticscraft.logisticsapi.item.InventoryStorage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

@EnergyStorage.EnergyStorageData(capacity = 10000)
@EnergyInput.EnergyInputData(maxReceive = 100)
@EnergyOutput.EnergyOutputData(maxExtract = 100)
@InventoryStorage.InventoryData(rows = 1, name = "TestBlock")
public class TestBlock extends LogisticBlock implements EnergyInput, EnergyOutput, InventoryStorage, InventoryHolder {

    // Running every 10 ticks
    @LogisticTickManager.Ticking(ticks = 10)
    public void update() {
        getBlock().ifPresent(block -> {
            block.setType(Material.WOOL);
            block.setData((byte) new Random().nextInt(15));
        });
    }

    @Override
    public InventoryHolder getInventoryHolder() {
        return this;
    }

    @Override
    public Inventory getInventory() {
        return getStoredInventory();
    }

    @Override
    public void onPlayerBreak(BlockBreakEvent event) {
        getBlock().ifPresent(block -> {
            for(ItemStack item : getInventory().getContents()){
                if(item != null && item.getType() != Material.AIR)
                    block.getWorld().dropItemNaturally(block.getLocation().add(0.5, 0.5, 0.5), item);
            }
            getInventory().setContents(new ItemStack[getInventory().getSize()]);
            event.setDropItems(false);
            getLogisticItem().ifPresent(item -> {
                block.getWorld().dropItemNaturally(block.getLocation().add(0.5, 0.5, 0.5), item.getItemStack(1));
            });
        });
    }

    @Override
    public void onLeftClick(PlayerInteractEvent event) {
        event.getPlayer().sendMessage("LeftClick");
    }

    @Override
    public void onRightClick(PlayerInteractEvent event) {
        if(!event.getPlayer().isSneaking()){
            event.getPlayer().openInventory(getInventory());
            event.setCancelled(true);
        }
    }

    @Override
    public void placeBlock(Player player, ItemStack item, Block block) {
        block.setType(Material.WOOL);
    }

}
