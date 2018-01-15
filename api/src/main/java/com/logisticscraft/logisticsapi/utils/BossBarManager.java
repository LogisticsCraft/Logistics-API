package com.logisticscraft.logisticsapi.utils;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

public class BossBarManager {

    public static BossBar create(String title, BarColor color, BarStyle style, BarFlag... flags) {
        return Bukkit.createBossBar(title, color, style, flags);
    }

}
