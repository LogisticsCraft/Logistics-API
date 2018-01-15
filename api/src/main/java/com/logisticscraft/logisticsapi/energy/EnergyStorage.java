package com.logisticscraft.logisticsapi.energy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Optional;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

import com.logisticscraft.logisticsapi.data.LogisticKey;
import com.logisticscraft.logisticsapi.data.holder.PersistentDataHolder;
import com.logisticscraft.logisticsapi.data.holder.VolatileDataHolder;
import com.logisticscraft.logisticsapi.utils.BossBarManager;
import com.logisticscraft.logisticsapi.utils.ReflectionUtils;

import lombok.Getter;
import lombok.Setter;

public interface EnergyStorage extends PersistentDataHolder, VolatileDataHolder {

    LogisticKey STORED_ENERGY_META_KEY = new LogisticKey("LogisticsAPI", "storedEnergy");
    LogisticKey STORED_BOSSBAR_KEY = new LogisticKey("LogisticsAPI", "bossEnergyBar");

    default long getMaxEnergyStored() {
        return ReflectionUtils.getClassAnnotation(this, EnergyStorageData.class).capacity();
    }

    default long getStoredEnergy() {
        synchronized (this) {
            return getPersistentData().get(STORED_ENERGY_META_KEY, Long.class).orElse(0L); 
        }
    }

    default void setStoredEnergy(final long energy) {
        synchronized (this) {
            final long newEnergy;
            if (energy > getMaxEnergyStored())
                newEnergy = getMaxEnergyStored();
            else if (energy < 0)
                newEnergy = 0;
            else
                newEnergy = energy;

            if (newEnergy == 0) {
                getPersistentData().remove(STORED_ENERGY_META_KEY);
                return;
            }

            getPersistentData().set(STORED_ENERGY_META_KEY, newEnergy);
        }
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface EnergyStorageData {

        int capacity();

    }

    ///////////////////////////////////////////////////////////////////////////
    // Energy Bar
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Gets the identification of EnergyBar.
     * It is used by .setupEnergyBar() but it's not essentials getValue the default value (null)
     * creates an anonymous BossBar, which is better from memory point, though in this case you would
     * have to configure .getEnergyBar() and .setEnergyBar(BossBar) to store {@link BossBar} locally for
     * this exact {@link EnergyStorage}.
     *
     * @return BossBar identification
     */
    default String getEnergyBarId() {
        return null;
    }

    /**
     * Gets Title for the EnergyBar.
     * Is used by default with .updateEnergyBar() so it can be dynamic.
     * By default displays amount of energy getValue <i>current_energy/max_energy</i>.
     *
     * @return title to be used for EnergyBar
     */
    default String getEnergyBarTitle() {
        return getStoredEnergy() + "/" + getMaxEnergyStored();
    }

    /**
     * Gets Progress for the EnergyBar.
     * Is used by default with .updateEnergyBar() so it can be dynamic.
     * By default displays amount of energy getValue <i>current_energy/max_energy</i>.
     *
     * @return progress ∈ [0;1] to be used for EnergyBar
     */
    default double getEnergyBarProgress() {
        return (double) getStoredEnergy() / (double) getMaxEnergyStored();
    }

    /**
     * Gets Color for the EnergyBar.
     * Is used by default with .updateEnergyBar() so it can be dynamic.
     * By default displays color according to rate of energy: <i>low - Red, normal - Yellow, full - Green</i>
     *
     * @return BarColor to be used for EnergyBar
     */
    default BarColor getEnergyBarColor() {
        switch ((int) (getEnergyBarProgress() * 2)) {
        case 0:
            return BarColor.RED;
        case 1:
            return BarColor.YELLOW;
        case 2:
            return BarColor.GREEN;
        }
        return BarColor.WHITE;
    }

    /**
     * Gets Style for the EnergyBar.
     * Is used by default with .updateEnergyBar() so it can be dynamic.
     * By default displays <i>SEGMENTED_20</i>
     *
     * @return BarStyle to be used for EnergyBar
     */
    default BarStyle getEnergyBarStyle() {
        return BarStyle.SEGMENTED_20;
    }

    /**
     * Gets Flags for the EnergyBar.
     * Is used by default with .updateEnergyBar() so it can be dynamic.
     * By default uses null (no additional styles) and is not recommended to be changed
     * getValue this is mostly for actual Bosses
     *
     * @return BarFlags to be used for EnergyBar
     */
    default BarFlag[] getEnergyBarFlags() {
        return new BarFlag[0];
    }

    /**
     * Gets the instance of {@link BossBar} representing Block's EnergyBar,
     * Used by {@link EnergyDisplayManager} to dynamically display it if Player is looking at this Block.
     * It's highly recommended to use it just getValue a regular {@link Getter}
     * together with .setEnergyBar() and .updateEnergyBar().
     *
     * @return instance of BossBar representing Block's EnergyBar
     */
    default Optional<BossBar> getEnergyBar() {
        return getVolatileData().get(STORED_BOSSBAR_KEY, BossBar.class);
    }

    /**
     * Sets the instance of {@link BossBar} representing Block's EnergyBar,
     * Used by {@link EnergyDisplayManager} to dynamically display it if Player is looking at this Block.
     * It's highly recommended to use it just getValue a regular {@link Setter}
     * together with .getEnergyBar() and .updateEnergyBar().
     *
     * @param bossBar instance of BossBar representing Block's EnergyBar
     */
    default void setEnergyBar(BossBar bossBar) {
        getVolatileData().set(STORED_BOSSBAR_KEY, bossBar);
    }

    /**
     * Generates rewrite's BossBar according to it's getters
     * No checks on ID being null are performed so created BossBar may be anonymous
     * @return Created EnergyBar
     */
    default Optional<BossBar> setupEnergyBar() {
        setEnergyBar(BossBarManager.create(getEnergyBarTitle(), getEnergyBarColor(), getEnergyBarStyle(), getEnergyBarFlags()));
        return getEnergyBar();
    }

    /**
     * Removes BossBar if it was registered by ID
     * Automatically checks whether ID is null though it's not recommended to have unnecessary calls
     */
    default void deleteEnergyBar() {
        // TODO: wtf is that?
        //if (getEnergyBarId() != null) BossBarManager.remove(getEnergyBarId());
    }

    /**
     * Updates visual information of BossBar
     * If BossBar is null than it tries to generate it using setupEnergyBar()
     * All checks on null are included and not necessary outside of method
     */
    default void updateEnergyBar() {
        Optional<BossBar> bossBar = getEnergyBar();
        if (!bossBar.isPresent()){
            bossBar = setupEnergyBar();
        }
        bossBar.ifPresent(energyBar -> {
            //Title
            if (!energyBar.getTitle().equals(getEnergyBarTitle())) energyBar.setTitle(getEnergyBarTitle());
            //Progress
            if (energyBar.getProgress() != getEnergyBarProgress()) energyBar.setProgress(getEnergyBarProgress());
            //Color
            if (!energyBar.getColor().equals(getEnergyBarColor())) energyBar.setColor(getEnergyBarColor());
            //Style
            if (!energyBar.getStyle().equals(getEnergyBarStyle())) energyBar.setStyle(getEnergyBarStyle());
        });
    }

}
