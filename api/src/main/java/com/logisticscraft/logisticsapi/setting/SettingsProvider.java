package com.logisticscraft.logisticsapi.setting;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import ch.jalu.configme.migration.PlainMigrationService;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

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
        File configFile = new File(dataFolder, "config.yml");
        return SettingsManagerBuilder.withYamlFile(configFile)
                .migrationService(new PlainMigrationService())
                .configurationData(SettingsProperties.class)
                .create();
    }
}
