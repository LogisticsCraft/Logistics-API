package com.logisticscraft.logisticsapi.rewrite.settings;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.Property;
import lombok.experimental.UtilityClass;

import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

@UtilityClass
public class SettingsProperties implements SettingsHolder {

    @Comment("Enables the console debug message logging.")
    public static final Property<Boolean> DEBUG_ENABLE = newProperty("debug.enable", false);

}
