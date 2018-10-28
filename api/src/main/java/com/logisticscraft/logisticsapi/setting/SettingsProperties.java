package com.logisticscraft.logisticsapi.setting;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.Property;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.UtilityClass;

import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SettingsProperties implements SettingsHolder {

    @Comment("Enables the console debug message logging.")
    public static final Property<Boolean> DEBUG_ENABLE = newProperty("debug.enable", false);
}
