package com.logisticscraft.logisticsapi;

import ch.jalu.configme.SettingsManager;
import ch.jalu.injector.Injector;
import ch.jalu.injector.InjectorBuilder;
import co.aikar.commands.BukkitCommandManager;

import com.logisticscraft.logisticsapi.api.BlockManager;
import com.logisticscraft.logisticsapi.api.ItemManager;
import com.logisticscraft.logisticsapi.block.LogisticBlockCache;
import com.logisticscraft.logisticsapi.block.LogisticBlockTypeRegister;
import com.logisticscraft.logisticsapi.block.LogisticTickManager;
import com.logisticscraft.logisticsapi.command.DebugCommands;
import com.logisticscraft.logisticsapi.listeners.BlockListener;
import com.logisticscraft.logisticsapi.listeners.ChunkListener;
import com.logisticscraft.logisticsapi.listeners.ItemListener;
import com.logisticscraft.logisticsapi.persistence.PersistenceStorage;
import com.logisticscraft.logisticsapi.settings.DataFolder;
import com.logisticscraft.logisticsapi.settings.SettingsProvider;
import com.logisticscraft.logisticsapi.utils.Tracer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Chunk;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import static com.logisticscraft.logisticsapi.settings.SettingsProperties.DEBUG_ENABLE;

@NoArgsConstructor
public final class LogisticsApi extends JavaPlugin {

    @Getter
    private static LogisticsApi instance;

    // Internal
    private Injector injector;
    private LogisticBlockCache blockCache;
    private LogisticTickManager tickManager;

    // API
    @Getter
    private BlockManager blockManager;
    @Getter
    private ItemManager itemManager;

    @Override
    public void onLoad() {
        instance = this;

        // Set the logger instance
        Tracer.setLogger(getLogger());
        Tracer.setDebug(false); // Disabled by default TODO: load from config

        // Prepare the injector
        injector = new InjectorBuilder().addDefaultHandlers("com.logisticscraft.logisticsapi").create();
        injector.register(LogisticsApi.class, instance);
        injector.register(Server.class, getServer());
        injector.register(PluginManager.class, getServer().getPluginManager());
        injector.register(BukkitScheduler.class, getServer().getScheduler());
        injector.provide(DataFolder.class, getDataFolder());
        injector.registerProvider(SettingsManager.class, SettingsProvider.class);

        // Load configuration
        SettingsManager settings = injector.getSingleton(SettingsManager.class);
        Tracer.setDebug(settings.getProperty(DEBUG_ENABLE));

        // Enable internal services
        injector.getSingleton(PersistenceStorage.class);
        tickManager = injector.getSingleton(LogisticTickManager.class);
        injector.getSingleton(LogisticBlockTypeRegister.class);
        blockCache = injector.getSingleton(LogisticBlockCache.class);

        // Create API
        blockManager = injector.getSingleton(BlockManager.class);
        itemManager = injector.getSingleton(ItemManager.class);
    }

    @Override
    public void onEnable() {
        // Print the greeting message and logo
        PluginDescriptionFile description = getDescription();
        Tracer.info(Constants.ASCII_LOGO);
        String authors = description.getAuthors().toString();
        Tracer.info("by: " + authors.substring(1, authors.length() - 1));
        Tracer.info("Server version: " + getServer().getVersion(), "Bukkit version: " + getServer().getBukkitVersion());

        // Load already loaded worlds
        for (World world : getServer().getWorlds()) {
            blockCache.registerWorld(world);
            for (Chunk chunk : world.getLoadedChunks()) {
                blockCache.loadSavedBlocks(chunk);
            }
        }

        // Register events
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(injector.getSingleton(ChunkListener.class), instance);
        pluginManager.registerEvents(injector.getSingleton(BlockListener.class), instance);
        pluginManager.registerEvents(injector.getSingleton(ItemListener.class), instance);

        // Start the tick manager task
        tickManager.runTaskTimer(this, 20, 1);

        // Register Commands
        BukkitCommandManager commmandManager = new BukkitCommandManager(this);
        commmandManager.registerCommand(injector.getSingleton(DebugCommands.class));

        Tracer.info(description.getName() + " (v" + description.getVersion() + ") has been enabled.");
    }

    @Override
    public void onDisable() {
        Tracer.info("Disabling...");

        for (World world : getServer().getWorlds()) {
            injector.getSingleton(LogisticBlockCache.class).unregisterWorld(world);
        }
        instance = null;

        PluginDescriptionFile description = getDescription();
        Tracer.info(description.getName() + " (v" + description.getVersion() + ") has been disabled.");
    }

}
