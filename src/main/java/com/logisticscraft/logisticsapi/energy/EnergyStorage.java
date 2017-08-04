package com.logisticscraft.logisticsapi.energy;

import com.logisticscraft.logisticsapi.LogisticObject;
import com.logisticscraft.logisticsapi.util.nms.bossbar.BossBarManager;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author JARvis (Пётр) PROgrammer
 */
public interface EnergyStorage extends LogisticObject {
    /**
     * Returns the {@link Location} of this {@link EnergyStorage}.
     * By default uses {@link EnergyManager}'s method to find it so is <b>highly recommended to be overridden</b>.
     * @return {@link Location} of the current {@link EnergyStorage}
     */
    default Location getEnergyStorageLocation() {
        return EnergyManager.getStorageLocation(this);
    }

    /**
     * Get the Share-Priority of block to describe it's ability to share energy with nearest blocks:
     * <ul>
     * <li><br>Blocks with similar priority <b>don't share</b> Energy</li>
     * <li><br>Blocks with lower priority <b>share</b> Energy with blocks of higher priority</li>
     * <li><br>Blocks with NEVER priority <b>don't share</b> Energy with any blocks</li>
     * <li><br>Blocks with ALWAYS priority <b>share</b> Energy with all blocks</li>
     * </ul>
     *
     * @return {@link EnergySharePriority} representing block's ability to share Energy
     */
    @Nonnull
    default EnergySharePriority getSharePriority() {
        return EnergySharePriority.NORMAL;
    }

    /**
     * Increases the amount of energy in storage by given value,
     * If the value is less or equal to the space available than 0 is returned,
     * Else positive value is returned.
     *
     * @param energy {@link long} amount of energy to be put into storage
     * @param force {@link boolean} whether or not to skip checking of EnergyStorage subtype
     * @return amount of energy not put into storage
     */
    default long putEnergy(long energy, boolean force) {
        if (!force && this instanceof EnergyProducer) return energy;

        long sum = this.getEnergy() + energy;

        if (getEnergy() < getMaxEnergy()) {
            if (sum <= getMaxEnergy()) {
                setEnergy(getEnergy() + energy);
                energy = 0;
            } else {
                energy = getEnergy() + energy - getMaxEnergy();
                setEnergy(getMaxEnergy());
            }
        }

        return energy;
    }

    /**
     * Decreases the amount of energy in storage by given value,
     * If the value is less or equal to the amount available than 0 is returned,
     * Else positive value is returned.
     *
     * @param energy {@link long} amount of energy to be taken from storage
     * @param force {@link boolean} whether or not to skip checking of EnergyStorage subtype
     * @return {@link long} amount of energy taken from storage
     */
    default long takeEnergy(long energy, boolean force) {
        if (!force && this instanceof EnergyConsumer) return 0;

        if (getEnergy() > 0) {
            long difference = getEnergy() - energy;
            if (difference >= 0) setEnergy(getEnergy() - energy); //Normal energy taking
            else { //Maximum possible energy taking
                energy = getEnergy();
                setEnergy(0);
            }
        } else energy = 0;

        return energy;
    }

    /**
     * Returns total energy in energy storage.
     *
     * @return {@link long} total energy in storage
     */
    long getEnergy();

    /**
     * Set absolute amount of Energy in Storage.
     *
     * @param energy {@link long} amount of energy to be set in storage
     */
    void setEnergy(long energy);

    /**
     * Gets maximal amount of energy which the block can store
     * Small not serious energy losses can happen if hard tests are not performes with this value,
     * e.g. if something produces <i>n</i> energy but the block requires <i>n-x</i> energy to be filled (<i>x less than 0</i>)
     * and so the block is filled to it's maximal possible storage and some energy disappears
     * "transforms" into thermal (or inner) energy.
     *
     * @return {@link long} maximal amount of energy which the block can store
     */
    long getMaxEnergy();

    /**
     * Gets maximal amount of energy which can be put from one side
     * Blocks with lower energy input would receive their maximal output
     * Blocks with higher energy input would receive senders's maximal energy output.
     *
     * @return {@link long} maximal amount of energy to be put from one side
     */
    long getMaxOutput();

    /**
     * Gets maximal amount of energy which can be taken at one side
     * Blocks with lower energy output would give their maximal output
     * Blocks with higher energy output would give receiver's maximal energy input
     *
     * @return {@link long} maximal amount of energy to be taken at one side.
     */
    long getMaxInput();

    /**
     * Gets all {@link Location}s of blocks which are touching this EnergyStorage,
     * Is actually a shorthand made for .takeEnergyNearby() in order not to overuse
     * processor time by Location calculations.
     *
     * @return all {@link Location}s of blocks which are touching this EnergyStorage
     */
    Location[] getSideLocations();
    void setSideLocations(@Nonnull Location[] sideLocations);

    /**
     * Sets up all SideLocations according to the location given
     * @param location {@link Location} from which to calculate SideLocations
     */
    default void setupSideLocations(Location location) {
        Location[] sideLocations = new Location[6];

        int i = 0;
        for (Vector vector : EnergyManager.SIDE_VECTORS.values()) {
            sideLocations[i] = location.clone().add(vector);
            i++;
        }

        setSideLocations(sideLocations);
    }

    /**
     * By default sets up all SideLocations according to the current .getEnergyStorageLocation()
     */
    default void setupSideLocations() {
        setupSideLocations(getEnergyStorageLocation());
    }


    /**
     * Takes energy from all nearest blocks (by default checks all blocks from .getSideLocations(),
     * Respects {@link EnergySharePriority}
     */
    default void takeEnergyNearby() {
        if (getEnergy() < getMaxEnergy()) {
            Location[] sideLocations = getSideLocations();

            if (sideLocations == null) return;

            for (Location location : sideLocations) {
                if (EnergyManager.isStorageAt(location)) {
                    EnergyStorage nearBlock = EnergyManager.getStorageAt(location);

                    assert (nearBlock != null);

                    //Check priority
                    //Priority NEVER
                    if (nearBlock.getSharePriority() == EnergySharePriority.NEVER
                            //Priority of block to take energy from is too low
                            || nearBlock.getSharePriority().ordinal() < getSharePriority().ordinal()) continue;

                    //Get amount available
                    long energy;
                    if (nearBlock.getMaxInput() > getMaxOutput()) energy = nearBlock.takeEnergy(getMaxOutput(), false);
                    else energy = nearBlock.takeEnergy(nearBlock.getMaxInput(), false);
                    putEnergy(energy, true);
                }
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Energy Bar
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Gets the identification of EnergyBar.
     * It is used by .setupEnergyBar() but it's not essentials as the default value (null)
     * creates an anonymous BossBar, which is better from memory point, though in this case you would
     * have to configure .getEnergyBar() and .setEnergyBar(BossBar) to store {@link BossBar} locally for
     * this exact {@link EnergyStorage}.
     *
     * @return BossBar identification as {@link String}
     */
    @Nullable
    @Getter
    default String getEnergyBarId() {
        return null;
    }

    /**
     * Gets Title for the EnergyBar.
     * Is used by default with .updateEnergyBar() so it can be dynamic.
     * By default displays amount of energy as <i>current_energy/max_energy</i>.
     *
     * @return {@link String} title to be used for EnergyBar
     */
    @Getter
    default String getEnergyBarTitle() {
        return getEnergy() + "/" + getMaxEnergy();
    }

    /**
     * Gets Progress for the EnergyBar.
     * Is used by default with .updateEnergyBar() so it can be dynamic.
     * By default displays amount of energy as <i>current_energy/max_energy</i>.
     *
     * @return {@link double} progress ∈ [0;1] to be used for EnergyBar
     */
    @Getter
    default double getEnergyBarProgress() {
        return (double) getEnergy() / (double) getMaxEnergy();
    }

    /**
     * Gets Color for the EnergyBar.
     * Is used by default with .updateEnergyBar() so it can be dynamic.
     * By default displays color according to rate of energy: <i>low - Red, normal - Yellow, full - Green</i>
     *
     * @return {@link BarColor} to be used for EnergyBar
     */
    @Nonnull
    @Getter
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
     * @return {@link BarStyle} to be used for EnergyBar
     */
    @Nonnull
    @Getter
    default BarStyle getEnergyBarStyle() {
        return BarStyle.SEGMENTED_20;
    }

    /**
     * Gets Flags for the EnergyBar.
     * Is used by default with .updateEnergyBar() so it can be dynamic.
     * By default uses null (no additional styles) and is not recommended to be changed
     * as this is mostly for actual Bosses
     *
     * @return {@link BarFlag}[] to be used for EnergyBar
     */
    @Nullable
    @Getter
    default BarFlag[] getEnergyBarFlags() {
        return null;
    }

    /**
     * Gets the instance of {@link BossBar} representing Block's EnergyBar,
     * Used by {@link EnergyManager} to dynamically display it if Player is looking at this Block.
     * It's highly recommended to use it just as a regular {@link Getter}
     * together with .setEnergyBar() and .updateEnergyBar().
     *
     * @return instance of {@link BossBar} representing Block's EnergyBar
     */
    @Nullable
    @Getter
    default BossBar getEnergyBar() {
        return null;
    }

    /**
     * Sets the instance of {@link BossBar} representing Block's EnergyBar,
     * Used by {@link EnergyManager} to dynamically display it if Player is looking at this Block.
     * It's highly recommended to use it just as a regular {@link Setter}
     * together with .getEnergyBar() and .updateEnergyBar().
     *
     * @param bossBar instance of {@link BossBar} representing Block's EnergyBar
     */
    @Setter
    default void setEnergyBar(@Nullable BossBar bossBar) {}

    /**
     * Generates block's BossBar according to it's getters
     * No checks on ID being null are performed so created BossBar may be anonymous
     */
    default void setupEnergyBar() {
        setEnergyBar(BossBarManager.getProvider().create(
                getEnergyBarId(), getEnergyBarTitle(), getEnergyBarColor(), getEnergyBarStyle(), getEnergyBarFlags())
        );
    }

    /**
     * Removes BossBar if it was registered by ID
     * Automatically checks wether ID is null though it's not recommended to have unnecessary calls
     */
    default void deleteEnergyBar() {
        if (getEnergyBarId() != null) BossBarManager.getProvider().remove(getEnergyBarId());
    }

    /**
     * Updates visual information of BossBar
     * If BossBar is null than it tries to generate it using setupEnergyBar()
     * All checks on null are included and not necessary outside of method
     */
    default void updateEnergyBar() {
        if (getEnergyBar() == null) setupEnergyBar();
        if (getEnergyBar() != null) {
            @Nonnull BossBar energyBar = getEnergyBar();
            //Title
            if (!energyBar.getTitle().equals(getEnergyBarTitle())) energyBar.setTitle(getEnergyBarTitle());
            //Progress
            if (energyBar.getProgress() != getEnergyBarProgress()) energyBar.setProgress(getEnergyBarProgress());
            //Color
            if (!energyBar.getColor().equals(getEnergyBarColor())) energyBar.setColor(getEnergyBarColor());
            //Style
            if (!energyBar.getStyle().equals(getEnergyBarStyle())) energyBar.setStyle(getEnergyBarStyle());
        }
    }
}
