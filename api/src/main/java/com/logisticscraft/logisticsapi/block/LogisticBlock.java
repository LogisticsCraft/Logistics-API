package com.logisticscraft.logisticsapi.block;

import com.logisticscraft.logisticsapi.data.LogisticKey;
import com.logisticscraft.logisticsapi.data.PersistentLogisticDataHolder;
import com.logisticscraft.logisticsapi.data.SafeBlockLocation;
import com.logisticscraft.logisticsapi.data.VolatileLogisticDataHolder;
import com.logisticscraft.logisticsapi.persistence.Persistent;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Optional;

/**
 * Represents a custom block handled by the API.
 */
public abstract class LogisticBlock implements PersistentLogisticDataHolder, VolatileLogisticDataHolder {

    @Getter
    @Persistent
    private LogisticKey typeId;

    @Getter
    @Persistent
    private SafeBlockLocation location;

    @Persistent
    private HashMap<LogisticKey, Object> persistentBlockData = new HashMap<>();
    private HashMap<LogisticKey, Object> volatileBlockData = new HashMap<>();

    /**
     * Set the persistent block property value with the given key.
     *
     * @param key   the property key
     * @param value the value
     * @param <T>   the value type
     */
    @Override
    public <T> void setPersistentLogisticData(@NonNull LogisticKey key, T value) {
        persistentBlockData.put(key, value);
    }

    /**
     * Removes the persistent block property with the given key.
     *
     * @param key the property key
     */
    @Override
    public void removePersistentLogisticData(@NonNull LogisticKey key) {
        persistentBlockData.remove(key);
    }

    /**
     * Get the persistent block property value with the given key.
     *
     * @param key  the property key
     * @param type the expected value type
     * @return the saved value
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> getPersistentLogisticData(@NonNull LogisticKey key, @NonNull Class<T> type) {
        return Optional.ofNullable((T) persistentBlockData.get(key));
    }


    /**
     * Set the volatile block property value with the given key.
     *
     * @param key   the property key
     * @param value the value
     * @param <T>   the value type
     */
    @Override
    public <T> void setVolatileLogisticData(@NonNull LogisticKey key, T value) {
        volatileBlockData.put(key, value);
    }

    /**
     * Removes the volatile block property with the given key.
     *
     * @param key the property key
     */
    @Override
    public void removeVolatileLogisticData(@NonNull LogisticKey key) {
        volatileBlockData.remove(key);
    }

    /**
     * Get the volatile block property value with the given key.
     *
     * @param key  the property key
     * @param type the expected value type
     * @return the saved value
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> getVolatileLogisticData(@NonNull LogisticKey key, @NonNull Class<T> type) {
        return Optional.ofNullable((T) volatileBlockData.get(key));
    }

    public void onPlayerBreak(BlockBreakEvent event) {
        // Overwrite to cancel blockbreaking. Uncanceled Blockbreak will remove the LogisticsBlock
    }

    public void onLeftClick(PlayerInteractEvent event) {

    }

    public void onRightClick(PlayerInteractEvent event) {

    }

}
