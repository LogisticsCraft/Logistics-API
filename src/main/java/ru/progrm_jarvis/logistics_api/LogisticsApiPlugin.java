package ru.progrm_jarvis.logistics_api;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import ru.progrm_jarvis.logistics_api.energy.EnergyManager;
import ru.progrm_jarvis.logistics_api.energy.wire.WireManager;
import ru.progrm_jarvis.logistics_api.util.console.Tracer;
import ru.progrm_jarvis.logistics_api.util.nms.NmsHelper;
import ru.progrm_jarvis.logistics_api.util.nms.bossbar.BossBarManager;

/**
 * @author JARvis (Пётр) PROgrammer
 */
public class LogisticsApiPlugin extends JavaPlugin {
    private static LogisticsApiPlugin instance;

    public static LogisticsApiPlugin getInstance() {
        return instance;
    }

    private static void disableAll() {
        Tracer.msg("Undisplaying EnergyBar for all Players...");
        EnergyManager.undisplayEnergyBarAll();
        Tracer.msg("EnergyBar for all Players has been displayed");
    }

    @Override
    public void onDisable() {
        Tracer.msg("Disabling...");

        disableAll();

        PluginDescriptionFile description = getDescription();
        Tracer.msg(description.getName() + " (v" + description.getVersion() + ") has been disabled.");
    }

    private static void enableNms() {
        Tracer.msg("Enabling NMS...");

        NmsHelper.setupVersion();

        BossBarManager.init();

        Tracer.msg("NMS has been enabled");
    }

    private static void enableEnergyManagers() {
        Tracer.msg("Enabling EnergyManagers...");

        EnergyManager.init();
        WireManager.init();

        Tracer.msg("EnergyManagers has been enabled");
    }

    @Override
    public void onEnable() {
        instance = this;

        Tracer.setLogger(getLogger());

        Tracer.msg("Enabling...");
        Tracer.msg("Server version: " + getServer().getVersion());
        Tracer.msg("Bukkit version: " + getServer().getBukkitVersion());

        enableNms();
        enableEnergyManagers();

        PluginDescriptionFile description = getDescription();
        Tracer.msg(description.getName() + " (v" + description.getVersion() + ") has been enabled.");
    }
}
