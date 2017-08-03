package ru.progrm_jarvis.logistics_api.util.nms.bossbar;

import org.bukkit.boss.BossBar;
import ru.progrm_jarvis.logistics_api.util.console.Tracer;
import ru.progrm_jarvis.logistics_api.util.nms.NmsHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JARvis (Пётр) PROgrammer
 */
public class BossBarManager {
    static Map<String, BossBar> bars = new HashMap<>();
    private static BossBarProvider provider = new BossBarProvider_1_12_R1();

    public static void init() {
        Tracer.msg("Enabling BossBar Manager...");
        try {
            setProvider((BossBarProvider) NmsHelper.getNmsProvider(
                    "ru.progrm_jarvis.logistics_api.util.nms.bossbar.BossBarProvider").newInstance()
            );
        } catch (InstantiationException | IllegalAccessException | NullPointerException e) {
            Tracer.msg("Error while enabling BossBarManager:");
            e.printStackTrace();
        }
        Tracer.msg("BossBar Manager has been enabled");
    }

    public static BossBarProvider getProvider() {
        return provider;
    }

    public static void setProvider(BossBarProvider provider) {
        BossBarManager.provider = provider;
    }
}
