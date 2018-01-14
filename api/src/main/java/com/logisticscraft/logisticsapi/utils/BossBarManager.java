package com.logisticscraft.logisticsapi.utils;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

import java.util.HashMap;
import java.util.Map;

public class BossBarManager {
    static Map<String, BossBar> bars = new HashMap<>();

    /**
     * Creates dynamic {@link BossBar} with given params
     *
     * @param id    BossBar identification for {@link BossBarManager}
     *              if null than anonymous BossBar is returned
     * @param title text title of the BossBar
     * @param color color enum of the BossBar
     * @param style style enum of the BossBar
     * @param flags flag(s) enums of the BossBar
     * @return created BossBar
     */
    public static BossBar create(String id,
                                 @NonNull String title,
                                 @NonNull BarColor color,
                                 @NonNull BarStyle style,
                                 BarFlag... flags) {
        if (flags == null) flags = new BarFlag[0];
        BossBar bossBar = Bukkit.createBossBar(title, color, style, flags);

        if (id != null) BossBarManager.bars.put(id, bossBar);

        return bossBar;
    }

    public static void remove(@NonNull String id) {
        BossBar bossBar = BossBarManager.bars.get(id);
        if (bossBar != null) {
            bossBar.removeAll();
            BossBarManager.bars.remove(id);
        }
    }
}
