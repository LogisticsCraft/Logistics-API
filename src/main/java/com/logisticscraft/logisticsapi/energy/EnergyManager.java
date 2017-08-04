package com.logisticscraft.logisticsapi.energy;

import com.logisticscraft.logisticsapi.LogisticsApiPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import com.logisticscraft.logisticsapi.util.console.Tracer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author JARvis (Пётр) PROgrammer
 */
public class EnergyManager {

    public static final Map<BlockSides, Vector> SIDE_VECTORS = new HashMap<BlockSides, Vector>() {{
        put(BlockSides.EAST, new Vector(1.0, 0.0, 0.0));
        put(BlockSides.WEST, new Vector(-1.0, 0.0, 0.0));
        put(BlockSides.UP, new Vector(0.0, 1.0, 0.0));
        put(BlockSides.DOWN, new Vector(0.0, -1.0, 0.0));
        put(BlockSides.SOUTH, new Vector(0.0, 0.0, 1.0));
        put(BlockSides.NORTH, new Vector(0.0, 0.0, -1.0));

    }};

    ///////////////////////////////////////////////////////////////////////////
    // Main EnergyManaging
    ///////////////////////////////////////////////////////////////////////////

    private static Map<Location, EnergyStorage> energyStorages = new HashMap<>();

    public static void registerEnergyStorage(@Nonnull final Location location,
                                             @Nonnull final EnergyStorage energyStorage) {
        if (energyStorages.putIfAbsent(location, energyStorage) == null) Tracer.msg(
                "Energy storage registered at " + location.toString()
        );
        else Tracer.msg("Energy storage re-registered at " + location.toString());

        energyStorage.setupSideLocations(location);
    }

    public static void unregisterEnergyStorage(@Nonnull final Location location) {
        if (energyStorages.remove(location) == null) Tracer.warn("Attempt to unregister unknown EnergyStorage");
    }

    public static boolean isStorageAt(@Nonnull final Location location) {
        return energyStorages.containsKey(location);
    }

    public static boolean isStorageRegistered(@Nonnull final EnergyStorage storage) {
        return energyStorages.containsValue(storage);
    }

    @Nullable
    public static EnergyStorage getStorageAt(@Nonnull final Location location) {
        return energyStorages.get(location);
    }

    @Nullable
    public static Location getStorageLocation(@Nonnull final EnergyStorage energyStorage) {
        for (Map.Entry<Location, EnergyStorage> entry : energyStorages.entrySet()) if (entry.getValue()
                == energyStorage) return entry.getKey();
        return null;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Sides
    ///////////////////////////////////////////////////////////////////////////

    public static void init() {
        setShowEnergyBarTask(Bukkit.getScheduler().runTaskTimer(LogisticsApiPlugin.getInstance(), () -> {
                    undisplayEnergyBarAll();
                    displayEnergyBarAll();
                }, 30L, 30L)
        );
    }

    public enum BlockSides {
        EAST, WEST, UP, DOWN, SOUTH, NORTH
    }

    ///////////////////////////////////////////////////////////////////////////
    // Energy DisplayBar
    ///////////////////////////////////////////////////////////////////////////

    private static BukkitTask showEnergyBarTask;
    private static Map<Player, BossBar> bars = new HashMap<>();

    public static BukkitTask getShowEnergyBarTask() {
        return showEnergyBarTask;
    }

    public static void setShowEnergyBarTask(BukkitTask showEnergyBarTask) {
        EnergyManager.showEnergyBarTask = showEnergyBarTask;
    }

    public static void displayEnergyInfo(@Nonnull Player player) {
        Block block = player.getTargetBlock((Set<Material>) null, 8);
        if (block != null) {

            EnergyStorage energyStorage = energyStorages.get(block.getLocation());
            if (energyStorage != null) {

                BossBar bar = energyStorage.getEnergyBar();
                if (bar != null) {
                    bar.addPlayer(player);
                    bars.put(player, bar);
                }
            }
        }
    }

    public static void displayEnergyBarAll() {
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        for (Player player : players) displayEnergyInfo(player);
    }

    public static void undisplayEnergyBar(@Nonnull Player player) {
        if (bars.containsKey(player)) {
            bars.get(player).removePlayer(player);
            bars.remove(player);
        }
    }

    public static void undisplayEnergyBarAll() {
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        for (Player player : players) undisplayEnergyBar(player);
    }

    /*
    TODO wires

    private static Set<Pair<Location, Location>> energyPairs = new HashSet<>();
    //private static Map<Player, EnergyStorage> wiring = new HashMap<>();
    private static Material wireMaterial = Material.LEASH;

    public static boolean isWiring(Player player) {
        return wiring.containsKey(player);
    }

    public static boolean isWireItem(ItemStack item) {
        return item != null && item.getType() == wireMaterial;
    }

    public static boolean tryWire(@Nonnull Player player, Block blockClicked) {
        if (blockClicked != null && isWireItem(player.getInventory().getItemInMainHand())) {
            SimpleWildBlock wildBlock = BlockManager.getWildBlockAtLocation(blockClicked.getEnergyStorageLocation());
            if (wildBlock != null && wildBlock instanceof EnergyStorage) {
                EnergyStorage energyBlock = (EnergyStorage) wildBlock;
                if (isWiring(player)) {
                    //Second point OR cancel
                    if (wiring.get(player).equals(blockClicked.getEnergyStorageLocation())) {
                        //Cancel
                        wiring.remove(player);
                        player.sendMessage("Cancelled wiring.");
                        return true;
                    } else {
                        //((Bat)wiring.get(player).getWirePoint().getPassengers().get(0))
                        //        .setLeashHolder(energyBlock.getWirePoint());
                        wiring.remove(player);
                        player.sendMessage("Wired");
                        return true;
                    }
                } else {
                    player.sendMessage("isNotW");
                    //First point
                    wiring.put(player, energyBlock);
                    player.sendMessage("Wiring...");
                    return true;
                }
            }
        }
        return false;
    }


    @Getter
    public static Set<Pair<Location, Location>> getEnergyPairs() {
        return energyPairs;
    }

    @Setter
    public static void setEnergyPairs(Set<Pair<Location, Location>> energyPairs) {
        EnergyManager.energyPairs = energyPairs;
    }*/
}
