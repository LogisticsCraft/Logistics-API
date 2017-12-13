package com.logisticscraft.logisticsapi.energy.storage;

import com.logisticscraft.logisticsapi.LogisticsApiPlugin;
import com.logisticscraft.logisticsapi.annotation.ApiComponent;
import com.logisticscraft.logisticsapi.rewrite.utils.Tracer;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

    @ApiComponent
    public static void registerEnergyStorage(@NonNull final Location location,
                                             @NonNull final EnergyStorage energyStorage) {
        if (energyStorages.putIfAbsent(location, energyStorage) == null) Tracer.info(
                "Energy storage registered at " + location.toString()
        );
        else Tracer.warn("Trying to register EnergyStorage at occupied location: " + location.toString());

        energyStorage.setupSideLocations(location);
    }

    @ApiComponent
    public static void unregisterEnergyStorage(@NonNull final Location location) {
        if (energyStorages.remove(location) == null) Tracer.warn("Attempt to unregister unknown EnergyStorage");
    }

    @ApiComponent
    public static boolean isStorageAt(@NonNull final Location location) {
        return energyStorages.containsKey(location);
    }

    @ApiComponent
    public static boolean isStorageRegistered(@NonNull final EnergyStorage storage) {
        return energyStorages.containsValue(storage);
    }

    @ApiComponent
    public static EnergyStorage getStorageAt(@NonNull final Location location) {
        return energyStorages.get(location);
    }

    @ApiComponent
    public static Location getStorageLocation(@NonNull final EnergyStorage energyStorage) {
        for (Map.Entry<Location, EnergyStorage> entry : energyStorages.entrySet())
            if (entry.getValue()
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

    public static void displayEnergyInfo(@NonNull Player player) {
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

    public static void undisplayEnergyBar(@NonNull Player player) {
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

    public static boolean tryWire(@NonNull Player player, Block blockClicked) {
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
