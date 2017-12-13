package com.logisticscraft.logisticsapi.util.nms;

import com.logisticscraft.logisticsapi.rewrite.utils.Tracer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

public class NmsHelper {

    @Getter
    @Setter
    private static NmsVersion version;

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
        Tracer.info("Setting up NmsHelper's version");
        setVersion(NmsVersion.valueOf(Bukkit.getServer().getClass().getPackage().getName()
                .replace(".", ",").split(",")[3].replace("v", "_"))
        );
        Tracer.info("NmsHelper's version has been set up");
    }

    public enum NmsVersion {
        _1_9_R1,
        _1_9_R2,
        _1_10_R1,
        _1_11_R1,
        _1_12_R1
    }

}
