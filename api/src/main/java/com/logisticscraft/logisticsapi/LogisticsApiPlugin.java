package com.logisticscraft.logisticsapi;

import com.logisticscraft.logisticsapi.energy.storage.EnergyManager;
import com.logisticscraft.logisticsapi.energy.wire.WireManager;
import com.logisticscraft.logisticsapi.general.command.CommandManager;
import com.logisticscraft.logisticsapi.liquid.FluidManager;
import com.logisticscraft.logisticsapi.util.console.Tracer;
import com.logisticscraft.logisticsapi.util.nms.NmsHelper;
import com.logisticscraft.logisticsapi.visual.BossBarManager;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author JARvis (Пётр) PROgrammer
 */
public final class LogisticsApiPlugin extends JavaPlugin {
    private static LogisticsApiPlugin instance;

    public static LogisticsApiPlugin getInstance() {
        return instance;
    }

    private static void disableAll() {
        Tracer.msg("Undisplaying EnergyBars for all Players...");
        EnergyManager.undisplayEnergyBarAll();
        Tracer.msg("EnergyBars for all Players has been undisplayed");
    }

    @Override
    public void onDisable() {
        Tracer.msg("Disabling...");

        disableAll();

        PluginDescriptionFile description = getDescription();
        Tracer.msg(description.getName() + " (v" + description.getVersion() + ") has been disabled.");
    }

    private static void enableEnergyManagers() {
        Tracer.msg("Enabling EnergyManagers...");

        EnergyManager.init();
        WireManager.init();

        Tracer.msg("EnergyManagers has been enabled");
    }

    private static void enableFluidManager() {
        Tracer.msg("Enabling FluidManager...");

        FluidManager.getFluid("water");

        Tracer.msg("FluidManager has been enabled");
    }

    private static void registerCommands() {
        Tracer.msg("Registering Commands...");

        CommandManager.registerCommands();

        Tracer.msg("Commands registered");
    }


    @Override
    public void onEnable() {
        instance = this;

        Tracer.setLogger(getLogger());

        PluginDescriptionFile description = getDescription();
        Tracer.traceLogo();
        String authors = description.getAuthors().toString();
        Tracer.msg("by: " + authors.substring(1, authors.length() - 1));
        Tracer.msg("Server version: " + getServer().getVersion(),
                "Bukkit version: " + getServer().getBukkitVersion());

        enableEnergyManagers();
        enableFluidManager();
        registerCommands();

        Tracer.msg(description.getName() + " (v" + description.getVersion() + ") has been enabled.");
    }
}
