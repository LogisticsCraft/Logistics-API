package com.logisticscraft.logisticsapi;

import ch.jalu.configme.SettingsManager;
import ch.jalu.injector.Injector;
import ch.jalu.injector.InjectorBuilder;
import co.aikar.commands.BukkitCommandManager;
import com.logisticscraft.logisticsapi.command.LogisticsApiCommand;
import com.logisticscraft.logisticsapi.energy.storage.EnergyManager;
import com.logisticscraft.logisticsapi.energy.wire.WireManager;
import com.logisticscraft.logisticsapi.liquid.FluidManager;
import com.logisticscraft.logisticsapi.settings.DataFolder;
import com.logisticscraft.logisticsapi.settings.SettingsProvider;
import com.logisticscraft.logisticsapi.util.logger.Tracer;
import lombok.Getter;
import org.bukkit.Server;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import static com.logisticscraft.logisticsapi.settings.SettingsProperties.DEBUG_ENABLE;

public final class LogisticsApiPlugin extends JavaPlugin {

    @Getter
    private static LogisticsApiPlugin instance;

    private Injector injector;
    private SettingsManager settings;
    private BukkitCommandManager commandManager;

    public LogisticsApiPlugin() {
    }

    @Override
    public void onEnable() {
        instance = this;

        // Set the logger instance
        Tracer.setLogger(getLogger());
        Tracer.setDebug(false); // Disabled by default TODO: load from config

        // Print the greeting message and logo
        PluginDescriptionFile description = getDescription();
        Tracer.info(Constants.ASCII_LOGO);
        String authors = description.getAuthors().toString();
        Tracer.info("by: " + authors.substring(1, authors.length() - 1));
        Tracer.info("Server version: " + getServer().getVersion(),
                "Bukkit version: " + getServer().getBukkitVersion());

        // Prepare the injector
        injector = new InjectorBuilder().addDefaultHandlers("com.logisticscraft.logisticsapi").create();
        injector.register(LogisticsApiPlugin.class, this);
        injector.register(Server.class, getServer());
        injector.register(BukkitScheduler.class, getServer().getScheduler());
        injector.provide(DataFolder.class, getDataFolder());
        injector.registerProvider(SettingsManager.class, SettingsProvider.class);

        // Load configuration
        settings = injector.getSingleton(SettingsManager.class);
        Tracer.setDebug(settings.getProperty(DEBUG_ENABLE));

        // Enable services
        enableEnergyManagers();
        enableFluidManager();
        registerCommands();

        Tracer.info(description.getName() + " (v" + description.getVersion() + ") has been enabled.");
    }

    private void registerCommands() {
        Tracer.info("Registering commands...");
        commandManager = new BukkitCommandManager(this);
        commandManager.registerCommand(new LogisticsApiCommand());
        Tracer.info("Commands registered");
    }

    private static void enableEnergyManagers() {
        Tracer.info("Enabling EnergyManagers...");
        EnergyManager.init();
        WireManager.init();
        Tracer.info("EnergyManagers has been enabled");
    }

    private static void enableFluidManager() {
        Tracer.info("Enabling FluidManager...");
        FluidManager.getFluid("water");
        Tracer.info("FluidManager has been enabled");
    }

    @Override
    public void onDisable() {
        Tracer.info("Disabling...");

        disableAll();

        PluginDescriptionFile description = getDescription();
        Tracer.info(description.getName() + " (v" + description.getVersion() + ") has been disabled.");
    }

    private void disableAll() {
        Tracer.info("Undisplaying EnergyBars for all Players...");
        EnergyManager.undisplayEnergyBarAll();
        Tracer.info("EnergyBars for all Players has been undisplayed");
    }

}
