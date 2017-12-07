package com.logisticscraft.logisticsapi.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import com.logisticscraft.logisticsapi.util.bukkit.LocationSerializer;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

@CommandAlias("logistics|lapi")
public class LogisticsApiCommand extends BaseCommand {

    @Subcommand("serializeloc|sloc")
    public void onSerializeLoc(Player player) {
        Block block = player.getTargetBlock(null, 32);
        if (block == null) {
            return;
        }

        Location location = block.getLocation();
        player.sendMessage("Default location:" + location.toString());
        String locationSerialized = LocationSerializer.serializeBlockLocation(location);
        player.sendMessage("Serialized: " + locationSerialized);
        player.sendMessage("DeSerialized (check): " + LocationSerializer
                .deserializeBlockLocation(locationSerialized));
    }

}
