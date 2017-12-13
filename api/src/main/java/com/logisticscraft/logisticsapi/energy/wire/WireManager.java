package com.logisticscraft.logisticsapi.energy.wire;

import com.logisticscraft.logisticsapi.annotation.ApiComponent;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import com.logisticscraft.logisticsapi.LogisticsApiPlugin;
import com.logisticscraft.logisticsapi.rewrite.utils.Tracer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WireManager {
    private static Map<Location, EnergyWire> wires = new HashMap<>();
    ///////////////////////////////////////////////////////////////////////////
    // Wire Ticking
    ///////////////////////////////////////////////////////////////////////////
    private static BukkitTask wireTickTask;

    public static void init() {
        wireTickTask = Bukkit.getScheduler().runTaskTimerAsynchronously(LogisticsApiPlugin.getInstance(), () -> {
            tickAllWires();
            for (Player player : Bukkit.getOnlinePlayers()) showWireInfo(player);
        }, 20L, 20L);
    }

    @ApiComponent
    public static void registerWire(@NonNull final Location location,
                                    @NonNull final EnergyWire wire) {
        if (wires.putIfAbsent(location, wire) == null) Tracer.info(
                "Energy Wire registered at " + location.toString()
        );
        else Tracer.warn("Trying to register EnergyWire at occupied location: " + location.toString());

        wire.setupSideLocations(location);
    }

    @ApiComponent
    public static void unregisterWire(@NonNull final Location location) {
        if (wires.remove(location) == null) Tracer.warn("Attempt to unregister unknown EnergyWire");
    }

    @ApiComponent
    public static boolean isWireAt(Location location) {
        return wires.containsKey(location);
    }

    @ApiComponent
    public static boolean isWireRegistered(EnergyWire wire) {
        return wires.containsValue(wire);
    }

    @ApiComponent
    public static EnergyWire getWireAt(Location location) {
        return wires.get(location);
    }

    private static void tickAllWires() {
        for (EnergyWire wire : wires.values()) {
            wire.updateWireNear();
        }
    }

    private static void showWireInfo(Player player) {
        Block block = player.getTargetBlock((Set<Material>) null, 16);
        if (block == null) return;
        Location location = block.getLocation();

        if (isWireAt(location)) {
            player.sendTitle(getWireAt(location).getEnergy() + ">>", "energy", 1, 18, 1);
        }
    }
}
