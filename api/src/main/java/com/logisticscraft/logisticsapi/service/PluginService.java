package com.logisticscraft.logisticsapi.service;

import com.logisticscraft.logisticsapi.LogisticsApi;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;

import javax.inject.Inject;
import java.util.Collection;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class PluginService {

    @Inject
    private Server server;

    @Inject
    private LogisticsApi plugin;

    public BukkitTask runTask(Runnable runnable) {
        return server.getScheduler().runTask(plugin, runnable);
    }

    public BukkitTask runTaskLater(Runnable runnable, long delay) {
        return server.getScheduler().runTaskLater(plugin, runnable, delay);
    }

    public BukkitTask runTaskTimer(Runnable runnable, long delay, long period) {
        return server.getScheduler().runTaskTimer(plugin, runnable, delay, period);
    }

    public BukkitTask runTaskAsynchronously(Runnable runnable) {
        return server.getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    public BukkitTask runTaskLaterAsynchronously(Runnable runnable, long delay) {
        return server.getScheduler().runTaskLaterAsynchronously(plugin, runnable, delay);
    }

    public BukkitTask runTaskTimerAsynchronously(Runnable runnable, long delay, long period) {
        return server.getScheduler().runTaskTimerAsynchronously(plugin, runnable, delay, period);
    }

    public Collection<? extends Player> getOnlinePlayers() {
        return server.getOnlinePlayers();
    }

    public void registerListener(Listener listener) {
        server.getPluginManager().registerEvents(listener, plugin);
    }

}
