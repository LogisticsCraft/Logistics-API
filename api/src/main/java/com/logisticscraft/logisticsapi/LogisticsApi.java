package com.logisticscraft.logisticsapi;

import ch.jalu.configme.SettingsManager;
import ch.jalu.injector.Injector;
import ch.jalu.injector.InjectorBuilder;
import com.logisticscraft.logisticsapi.block.LogisticBlockManager;
import com.logisticscraft.logisticsapi.persistence.PersistenceStorage;
import com.logisticscraft.logisticsapi.settings.DataFolder;
import com.logisticscraft.logisticsapi.settings.SettingsProvider;
import com.logisticscraft.logisticsapi.utils.Tracer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import static com.logisticscraft.logisticsapi.settings.SettingsProperties.DEBUG_ENABLE;

@NoArgsConstructor
public final class LogisticsApi extends JavaPlugin {

    @Getter
    private static LogisticsApi instance;

    // Internal
    private Injector injector;
    private SettingsManager settings;

    // API

    @Override
    public void onEnable() {
        instance = this;

        // Set the logger instance
        Tracer.setLogger(getLogger());
        Tracer.setDebug(false); // Disabled by default TODO: load from config

        // Print the greeting message and logo
        val description = getDescription();
        Tracer.info(Constants.ASCII_LOGO);
        val authors = description.getAuthors().toString();
        Tracer.info("by: " + authors.substring(1, authors.length() - 1));
        Tracer.info("Server version: " + getServer().getVersion(),
                "Bukkit version: " + getServer().getBukkitVersion());

        // Prepare the injector
        injector = new InjectorBuilder().addDefaultHandlers("com.logisticscraft.logisticsapi").create();
        injector.register(LogisticsApi.class, this);
        injector.register(Server.class, getServer());
        injector.register(BukkitScheduler.class, getServer().getScheduler());
        injector.provide(DataFolder.class, getDataFolder());
        injector.registerProvider(SettingsManager.class, SettingsProvider.class);

        // Load configuration
        settings = injector.getSingleton(SettingsManager.class);
        Tracer.setDebug(settings.getProperty(DEBUG_ENABLE));

        // Enable internal services
        injector.getSingleton(PersistenceStorage.class);
        injector.getSingleton(LogisticBlockManager.class);

        Tracer.info(description.getName() + " (v" + description.getVersion() + ") has been enabled.");
    }

    @Override
    public void onDisable() {
        Tracer.info("Disabling...");

        // TODO: stuff
        instance = null;

        val description = getDescription();
        Tracer.info(description.getName() + " (v" + description.getVersion() + ") has been disabled.");
    }

}
