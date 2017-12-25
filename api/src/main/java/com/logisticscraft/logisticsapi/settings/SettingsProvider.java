package com.logisticscraft.logisticsapi.settings;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.migration.PlainMigrationService;
import ch.jalu.configme.resource.PropertyResource;
import ch.jalu.configme.resource.YamlFileResource;
import com.logisticscraft.logisticsapi.utils.FileUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.File;

/**
 * Initializes the settings.
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class SettingsProvider implements Provider<SettingsManager> {

    @Inject
    @DataFolder
    private File dataFolder;

    /**
     * Loads the plugin's settings.
     *
     * @return the settings instance, or null if it could not be constructed
     */
    @Override
    public SettingsManager get() {
        val configFile = new File(dataFolder, "config.yml");
        if (!configFile.exists()) {
            FileUtils.create(configFile);
        }
        val resource = new YamlFileResource(configFile);
        return new SettingsManager(resource, new PlainMigrationService(), SettingsProperties.class);
    }

}
