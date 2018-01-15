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
    private static Map<String, BossBar> bars = new HashMap<>();


    public static void remove(@NonNull String id) {
        BossBar bossBar = BossBarManager.bars.get(id);
        if (bossBar != null) {
            bossBar.removeAll();
            BossBarManager.bars.remove(id);
        }
    }
}
