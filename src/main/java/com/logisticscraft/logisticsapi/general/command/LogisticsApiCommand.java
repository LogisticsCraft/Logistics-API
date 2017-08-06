package com.logisticscraft.logisticsapi.general.command;

import com.logisticscraft.logisticsapi.util.location.LocationManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * @author JARvis (Пётр) PROgrammer
 */
public class LogisticsApiCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (args[0].equalsIgnoreCase("sloc") && sender instanceof Player) {
            Block block = ((Player) sender).getTargetBlock((Set<Material>) null, 32);
            if (block != null) {
                Location location = block.getLocation();

                sender.sendMessage("Default location:" + location.toString());
                String locationSerialized = LocationManager.serializeLocation(location);
                sender.sendMessage("Serialized: " + locationSerialized);
                sender.sendMessage("DeSerialized (check): " + LocationManager
                        .deserializeLocation(locationSerialized));
            }
        }
        return false;
    }
}
