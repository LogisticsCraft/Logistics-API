package com.logisticsapi.util.nms;

import org.bukkit.Bukkit;

import com.logisticsapi.util.console.Tracer;

import javax.annotation.Nullable;

/**
 * @author JARvis (Пётр) PROgrammer
 */
public class NmsHelper {
    private static NmsVersion version;

    public static NmsVersion getVersion() {
        return version;
    }

    public static void setVersion(NmsVersion version) {
        NmsHelper.version = version;
    }

    @Nullable
    public static Class getNmsProvider(String name) {
        Class providerClass = null;
        try {
            providerClass = Class.forName(name + version.name());
        } catch (ClassNotFoundException e) {
            Tracer.err("Could not load NmsProvider class: " + name);
            e.printStackTrace();
        }
        return providerClass;
    }


    public static void setupVersion() {
        Tracer.msg("Setting up NmsHelper's version");
        setVersion(NmsVersion.valueOf(Bukkit.getServer().getClass().getPackage().getName()
                .replace(".", ",").split(",")[3].replace("v", "_"))
        );
        Tracer.msg("NmsHelper's version has been set up");
    }

    public enum NmsVersion {
        _1_9_R1, _1_9_R2, _1_10_R1, _1_11_R1, _1_12_R1
    }
}
