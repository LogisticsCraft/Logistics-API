package com.logisticscraft.logisticsapi.energy;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import com.logisticscraft.logisticsapi.LogisticsApi;
import com.logisticscraft.logisticsapi.block.LogisticBlock;
import com.logisticscraft.logisticsapi.block.LogisticBlockCache;

import lombok.NonNull;

public class EnergyDisplay {

    @Inject
    private LogisticBlockCache blockCache;
    
    private BukkitTask showEnergyBarTask;
    private Map<Player, BossBar> bars = new HashMap<>();
    
    public EnergyDisplay() {
        setShowEnergyBarTask(Bukkit.getScheduler().runTaskTimer(LogisticsApi.getInstance(), () -> {
                    undisplayEnergyBarAll();
                    displayEnergyBarAll();
                }, 30L, 30L)
        );
    }

    public BukkitTask getShowEnergyBarTask() {
        return showEnergyBarTask;
    }

    public void setShowEnergyBarTask(BukkitTask showEnergyBarTask) {
        this.showEnergyBarTask = showEnergyBarTask;
    }

    public void displayEnergyInfo(@NonNull Player player) {
        Block block = player.getTargetBlock((Set<Material>) null, 8);
        if (block != null) {

            Optional<LogisticBlock> logisticBlock = blockCache.getCachedLogisticBlockAt(block.getLocation());
            logisticBlock.filter(b ->  b instanceof EnergyStorage).ifPresent(energyStorage -> {
                ((EnergyStorage) energyStorage).updateEnergyBar();
                Optional<BossBar> bBar = ((EnergyStorage) energyStorage).getEnergyBar();
                bBar.ifPresent(bar -> {
                    bar.addPlayer(player);
                    bars.put(player, bar);
                });
            });
        }
    }

    public void displayEnergyBarAll() {
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        for (Player player : players) displayEnergyInfo(player);
    }

    public void undisplayEnergyBar(@NonNull Player player) {
        if (bars.containsKey(player)) {
            bars.get(player).removePlayer(player);
            bars.remove(player);
        }
    }

    public void undisplayEnergyBarAll() {
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        for (Player player : players) undisplayEnergyBar(player);
    }
    
}
