package com.logisticscraft.logisticsapi.energy;

import com.logisticscraft.logisticsapi.block.LogisticBlockCache;
import com.logisticscraft.logisticsapi.service.PluginService;
import com.logisticscraft.logisticsapi.service.shutdown.ShutdownHandler;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bukkit.block.Block;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class EnergyDisplayManager extends BukkitRunnable implements ShutdownHandler {

    @Inject
    private PluginService pluginService;

    @Inject
    private LogisticBlockCache blockCache;

    private Map<Player, BossBar> bars = new HashMap<>();

    @Override
    public void run() {
        unDisplayEnergyBars();
        pluginService.getOnlinePlayers().forEach(this::displayEnergyInfo);
    }

    @Override
    public void handleShutdown() {
        cancel();
        unDisplayEnergyBars();
    }

    private void unDisplayEnergyBars() {
        bars.forEach(((player, bossBar) -> bossBar.removePlayer(player)));
        bars.clear();
    }

    private void displayEnergyInfo(@NonNull Player player) {
        Block targetBlock = player.getTargetBlock(null, 8);
        if (targetBlock == null) {
            return;
        }

        blockCache.getCachedLogisticBlockAt(targetBlock.getLocation()).ifPresent(block -> {
            if (!(block instanceof EnergyStorage)) {
                return;
            }

            EnergyStorage energyStorage = (EnergyStorage) block;
            energyStorage.updateEnergyBar();
            Optional<BossBar> bossBar = energyStorage.getEnergyBar();
            bossBar.ifPresent(bar -> {
                bar.addPlayer(player);
                bars.put(player, bar);
            });
        });
    }

}
