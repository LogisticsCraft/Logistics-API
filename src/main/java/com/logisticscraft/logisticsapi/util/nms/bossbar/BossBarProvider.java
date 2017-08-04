package com.logisticscraft.logisticsapi.util.nms.bossbar;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author JARvis (Пётр) PROgrammer
 */
public interface BossBarProvider {
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
    BossBar create(@Nullable String id,
                   @Nonnull String title,
                   @Nonnull BarColor color,
                   @Nonnull BarStyle style,
                   BarFlag... flags);

    void remove(@Nonnull String id);
}
