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
import com.logisticscraft.logisticsapi.energy.EnergyDisplayManager;
import com.logisticscraft.logisticsapi.item.CraftingManager;
import com.logisticscraft.logisticsapi.listener.BlockListener;
import com.logisticscraft.logisticsapi.listener.ChunkListener;
import com.logisticscraft.logisticsapi.listener.ItemListener;
import com.logisticscraft.logisticsapi.persistence.PersistenceStorage;
import com.logisticscraft.logisticsapi.service.PluginService;
import com.logisticscraft.logisticsapi.service.shutdown.ShutdownListenerService;
import com.logisticscraft.logisticsapi.setting.DataFolder;
import com.logisticscraft.logisticsapi.setting.SettingsProvider;
import com.logisticscraft.logisticsapi.util.Tracer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Chunk;
import org.bukkit.Server;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import static com.logisticscraft.logisticsapi.setting.SettingsProperties.DEBUG_ENABLE;

@NoArgsConstructor
public final class LogisticsApi extends JavaPlugin {

    @Getter
    private static LogisticsApi instance;

    // Internal
    private Injector injector;
    private ShutdownListenerService shutdownHandlerService;
    private LogisticBlockCache blockCache;
    private LogisticTickManager tickManager;
    private EnergyDisplayManager energyDisplayManager;

    // API
    @Getter
    private BlockManager blockManager;
    @Getter
    private ItemManager itemManager;
    @Getter
    private PersistenceStorage persistenceStorage;

    @Override
    public void onLoad() {
        instance = this;

        // Set the logger instance
        Tracer.setLogger(getLogger());

        // Prepare the injector
        injector = new InjectorBuilder().addDefaultHandlers("com.logisticscraft.logisticsapi").create();
        injector.register(LogisticsApi.class, instance);
        injector.register(Server.class, getServer());
        injector.register(PluginManager.class, getServer().getPluginManager());

        // Load configuration
        injector.provide(DataFolder.class, getDataFolder());
        injector.registerProvider(SettingsManager.class, SettingsProvider.class);
        SettingsManager settings = injector.getSingleton(SettingsManager.class);
        Tracer.setDebug(settings.getProperty(DEBUG_ENABLE));

        // Enable internal services
        injector.getSingleton(PluginService.class);
        shutdownHandlerService = injector.getSingleton(ShutdownListenerService.class);
        persistenceStorage = injector.getSingleton(PersistenceStorage.class);
        tickManager = injector.getSingleton(LogisticTickManager.class);
        injector.getSingleton(LogisticBlockTypeRegister.class);
        blockCache = injector.getSingleton(LogisticBlockCache.class);
        injector.getSingleton(CraftingManager.class);
        energyDisplayManager = injector.getSingleton(EnergyDisplayManager.class);

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
        getServer().getWorlds().forEach(world -> {
            blockCache.registerWorld(world);
            for (Chunk chunk : world.getLoadedChunks()) {
                blockCache.loadSavedBlocks(chunk);
            }
        });

        // Register events
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(injector.getSingleton(ChunkListener.class), instance);
        pluginManager.registerEvents(injector.getSingleton(BlockListener.class), instance);
        pluginManager.registerEvents(injector.getSingleton(ItemListener.class), instance);

        // Start tasks
        tickManager.runTaskTimer(this, 20L, 1L);
        energyDisplayManager.runTaskTimer(this, 30L, 30L);

        // Register Commands
        BukkitCommandManager commandManager = new BukkitCommandManager(this);
        injector.register(BukkitCommandManager.class, commandManager);
        commandManager.registerCommand(injector.getSingleton(DebugCommands.class));

        Tracer.info(description.getName() + " (v" + description.getVersion() + ") has been enabled.");
    }

    @Override
    public void onDisable() {
        Tracer.info("Disabling...");

        // Invalidate the instance
        instance = null;

        // Shutdown components
        shutdownHandlerService.shutdownComponents();

        // Unload the worlds in the end, to prevent shutdownComponents order issues
        getServer().getWorlds().forEach(world -> blockCache.unregisterWorld(world));

        PluginDescriptionFile description = getDescription();
        Tracer.info(description.getName() + " (v" + description.getVersion() + ") has been disabled.");
    }
}
