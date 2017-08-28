package com.logisticscraft.logisticsapi.util.nms.bossbar;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_12_R1.boss.CraftBossBar;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author JARvis (Пётр) PROgrammer
 */
public class BossBarProvider_1_12_R1 implements BossBarProvider {
    @Override
    public BossBar create(@Nullable String id,
                          @Nonnull String title,
                          @Nonnull BarColor color,
                          @Nonnull BarStyle style,
                          @Nullable BarFlag... flags) {
        if (flags == null) flags = new BarFlag[0];
        BossBar bossBar = new CraftBossBar(title, color, style, flags);

        if (id != null) BossBarManager.bars.put(id, bossBar);

        return bossBar;
    }

    @Override
    public void remove(@Nonnull String id) {
        BossBar bossBar = BossBarManager.bars.get(id);
        if (bossBar != null) {
            bossBar.removeAll();
            BossBarManager.bars.remove(id);
        }
    }
}
