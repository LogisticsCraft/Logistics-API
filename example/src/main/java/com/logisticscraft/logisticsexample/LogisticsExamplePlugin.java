package com.logisticscraft.logisticsexample;

import com.logisticscraft.logisticsloader.LogisticInstallException;
import com.logisticscraft.logisticsloader.LogisticsLoader;
import org.bukkit.plugin.java.JavaPlugin;

public final class LogisticsExamplePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        try {
            if(LogisticsLoader.install()) {
                getLogger().info("The required library has been downloaded correctly!");
            }
        } catch (LogisticInstallException e) {
            e.printStackTrace();
            getLogger().severe("Unable to download the required library, disabling...");
            setEnabled(false);
        }
    }

}
