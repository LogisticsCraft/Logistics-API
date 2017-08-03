package ru.progrm_jarvis.logistics_api.energy.wire;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import ru.progrm_jarvis.logistics_api.LogisticsApiPlugin;
import ru.progrm_jarvis.logistics_api.util.console.Tracer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author JARvis (Пётр) PROgrammer
 * TODO
 */
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

    public static void registerWire(@Nonnull final Location location,
                                    @Nonnull final EnergyWire wire) {
        if (wires.putIfAbsent(location, wire) == null) Tracer.msg(
                "Energy Wire registered at " + location.toString()
        );
        else Tracer.msg("Energy Wire re-registered at " + location.toString());

        wire.setupSideLocations(location);
    }

    public static void unregisterWire(@Nonnull final Location location) {
        if (wires.remove(location) == null) Tracer.warn("Attempt to unregister unknown EnergyWire");
    }

    public static boolean isWireAt(Location location) {
        return wires.containsKey(location);
    }

    public static boolean isWireRegistered(EnergyWire wire) {
        return wires.containsValue(wire);
    }

    @Nullable
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
            player.sendTitle(getWireAt(location).getWireEnergy() + ">>", "energy", 1, 18, 1);
        }
    }
}
