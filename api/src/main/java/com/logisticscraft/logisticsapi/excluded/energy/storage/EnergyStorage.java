package com.logisticscraft.logisticsapi.excluded.energy.storage;

import com.logisticscraft.logisticsapi.excluded.energy.wire.EnergyWire;
import com.logisticscraft.logisticsapi.excluded.util.bukkit.BlockSide;
import com.logisticscraft.logisticsapi.excluded.visual.BossBarManager;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.util.Vector;

import java.util.Set;

public interface EnergyStorage extends EnergyWire {
    /**
     * Returns the {@link Location} of this {@link EnergyStorage}.
     * By default uses {@link EnergyManager}'s method to find it so is <b>highly recommended to be overridden</b>.
     *
     * @return location of the current {@link EnergyStorage}
     */
    default Location getEnergyStorageLocation() {
        return EnergyManager.getStorageLocation(this);
    }

    /**
     * Get the Share-Priority of rewrite to describe it's ability to share energy with nearest blocks:
     * <ul>
     * <li><br>Blocks with similar priority <b>don't share</b> Energy</li>
     * <li><br>Blocks with lower priority <b>share</b> Energy with blocks of higher priority</li>
     * <li><br>Blocks with NEVER priority <b>don't share</b> Energy with any blocks</li>
     * <li><br>Blocks with ALWAYS priority <b>share</b> Energy with all blocks</li>
     * </ul>
     *
     * @return priority representing rewrite's ability to share Energy
     */
    @NonNull
    default EnergySharePriority getEnergySharePriority() {
        return EnergySharePriority.NORMAL;
    }

    /**
     * Decreases the amount of energy in storage by given value,
     * If the value is less or equal to the amount available than 0 is returned,
     * Else positive value is returned.
     *
     * @param energy amount of energy to be taken from storage
     * @param force  whether or not to skip checking of EnergyStorage subtype
     * @return amount of energy taken from storage
     */
    default long takeEnergy(long energy, boolean force) {
        if (!force && this.getEnergySharePriority() == EnergySharePriority.NEVER) return 0;

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
     * Increases the amount of energy in storage by given value,
     * If the value is less or equal to the space available than 0 is returned,
     * Else positive value is returned.
     *
     * @param energy amount of energy to be put into storage
     * @param force  whether or not to skip checking of EnergyStorage subtype
     * @return amount of energy not put into storage
     */
    default long putEnergy(long energy, boolean force) {
        if (!force && this.getEnergySharePriority() == EnergySharePriority.ALWAYS) return energy;

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
     * Returns sides from which Energy can be taken
     *
     * @return all sides from which Energy can be taken
     */
    default Set<BlockSide> getEnergyTakeSides() {
        return BlockSide.ALL_SIDES;
    }

    /**
     * Returns sides to which Energy can be taken
     *
     * @return all sides to which Energy can be put
     */
    default Set<BlockSide> getEnergyPutSides() {
        return BlockSide.ALL_SIDES;
    }

    /**
     * Takes energy from the {@link BlockSide} given
     *
     * @param energy    amount of energy to be taken from storage
     * @param blockSide from which side to take energy
     * @param force     whether or not to skip checking of EnergyStorage subtype
     * @return amount of energy taken from storage
     */
    default long takeEnergy(long energy, @NonNull BlockSide blockSide, boolean force) {
        if (getEnergy() <= 0 || !getEnergyTakeSides().contains(blockSide)) return 0;
        return takeEnergy(energy, force);
    }

    /**
     * Puts energy to the {@link BlockSide} given
     *
     * @param energy    amount of energy to be put into storage
     * @param blockSide to which side to put energy
     * @param force     whether or not to skip checking of EnergyStorage subtype
     * @return amount of energy not put into storage
     */
    default long putEnergy(long energy, @NonNull BlockSide blockSide, boolean force) {
        if (getEnergy() >= getMaxEnergy() || !getEnergyPutSides().contains(blockSide)) return 0;
        return takeEnergy(energy, force);
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
     * Gets maximal amount of energy which the rewrite can store
     * Small not serious energy losses can happen if hard tests are not performes with this value,
     * e.g. if something produces <i>n</i> energy but the rewrite requires <i>n-x</i> energy to be filled (<i>x less than 0</i>)
     * and so the rewrite is filled to it's maximal possible storage and some energy disappears
     * "transforms" into thermal (or inner) energy.
     *
     * @return {@link long} maximal amount of energy which the rewrite can store
     */
    long getMaxEnergy();

    /**
     * Gets maximal amount of energy which can be put from one side
     * Blocks with lower energy input would receive their maximal output
     * Blocks with higher energy input would receive senders's maximal energy output.
     *
     * @return maximal amount of energy to be put from one side
     */
    long getMaxOutput();

    /**
     * Gets maximal amount of energy which can be taken at one side
     * Blocks with lower energy output would give their maximal output
     * Blocks with higher energy output would give receiver's maximal energy input
     *
     * @return maximal amount of energy to be taken at one side.
     */
    long getMaxInput();

    /**
     * Gets all {@link Location}s of blocks which are touching this EnergyStorage,
     * Is actually a shorthand made for .takeEnergyNearby() in order not to overuse
     * processor time by Location calculations.
     *
     * @return all locations of blocks which are touching this EnergyStorage
     */
    Location[] getSideLocations();

    void setSideLocations(@NonNull Location[] sideLocations);

    /**
     * Sets up all SideLocations according to the location given
     *
     * @param location location from which to calculate SideLocations
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
                    if (nearBlock.getEnergySharePriority() == EnergySharePriority.NEVER
                            //Priority of rewrite to take energy from is too low
                            || nearBlock.getEnergySharePriority().ordinal() < getEnergySharePriority().ordinal())
                        continue;

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
     * It is used by .setupEnergyBar() but it's not essentials getValue the default value (null)
     * creates an anonymous BossBar, which is better from memory point, though in this case you would
     * have to configure .getEnergyBar() and .setEnergyBar(BossBar) to store {@link BossBar} locally for
     * this exact {@link EnergyStorage}.
     *
     * @return BossBar identification
     */
    @Getter
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
    @Getter
    default String getEnergyBarTitle() {
        return getEnergy() + "/" + getMaxEnergy();
    }

    /**
     * Gets Progress for the EnergyBar.
     * Is used by default with .updateEnergyBar() so it can be dynamic.
     * By default displays amount of energy getValue <i>current_energy/max_energy</i>.
     *
     * @return progress ∈ [0;1] to be used for EnergyBar
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
     * @return BarColor to be used for EnergyBar
     */
    @NonNull
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
     * @return BarStyle to be used for EnergyBar
     */
    @NonNull
    @Getter
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
    @Getter
    default BarFlag[] getEnergyBarFlags() {
        return null;
    }

    /**
     * Gets the instance of {@link BossBar} representing Block's EnergyBar,
     * Used by {@link EnergyManager} to dynamically display it if Player is looking at this Block.
     * It's highly recommended to use it just getValue a regular {@link Getter}
     * together with .setEnergyBar() and .updateEnergyBar().
     *
     * @return instance of BossBar representing Block's EnergyBar
     */
    @Getter
    default BossBar getEnergyBar() {
        return null;
    }

    /**
     * Sets the instance of {@link BossBar} representing Block's EnergyBar,
     * Used by {@link EnergyManager} to dynamically display it if Player is looking at this Block.
     * It's highly recommended to use it just getValue a regular {@link Setter}
     * together with .getEnergyBar() and .updateEnergyBar().
     *
     * @param bossBar instance of BossBar representing Block's EnergyBar
     */
    @Setter
    default void setEnergyBar(BossBar bossBar) {
    }

    /**
     * Generates rewrite's BossBar according to it's getters
     * No checks on ID being null are performed so created BossBar may be anonymous
     */
    default void setupEnergyBar() {
        setEnergyBar(BossBarManager.create(
                getEnergyBarId(), getEnergyBarTitle(), getEnergyBarColor(), getEnergyBarStyle(), getEnergyBarFlags())
        );
    }

    /**
     * Removes BossBar if it was registered by ID
     * Automatically checks whether ID is null though it's not recommended to have unnecessary calls
     */
    default void deleteEnergyBar() {
        if (getEnergyBarId() != null) BossBarManager.remove(getEnergyBarId());
    }

    /**
     * Updates visual information of BossBar
     * If BossBar is null than it tries to generate it using setupEnergyBar()
     * All checks on null are included and not necessary outside of method
     */
    default void updateEnergyBar() {
        if (getEnergyBar() == null) setupEnergyBar();
        if (getEnergyBar() != null) {
            @NonNull BossBar energyBar = getEnergyBar();
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
