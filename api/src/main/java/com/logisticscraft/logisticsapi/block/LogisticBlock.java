package com.logisticscraft.logisticsapi.block;

import com.logisticscraft.logisticsapi.data.LogisticKey;
import com.logisticscraft.logisticsapi.data.LogisticPersistentDataHolder;
import com.logisticscraft.logisticsapi.data.LogisticVolatileDataHolder;
import com.logisticscraft.logisticsapi.data.SafeBlockLocation;
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
public abstract class LogisticBlock implements LogisticPersistentDataHolder, LogisticVolatileDataHolder {

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
    public <T> void setLogisticPersistentData(@NonNull LogisticKey key, T value) {
        persistentBlockData.put(key, value);
    }

    /**
     * Removes the persistent block property with the given key.
     *
     * @param key the property key
     */
    @Override
    public void removeLogisticPersistentData(@NonNull LogisticKey key) {
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
    public <T> Optional<T> getLogisticPersistentData(@NonNull LogisticKey key, @NonNull Class<T> type) {
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
    public <T> void setLogisticVolatileData(@NonNull LogisticKey key, T value) {
        persistentBlockData.put(key, value);
    }

    /**
     * Removes the volatile block property with the given key.
     *
     * @param key the property key
     */
    @Override
    public void removeLogisticVolatileData(@NonNull LogisticKey key) {
        persistentBlockData.remove(key);
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
    public <T> Optional<T> getLogisticVolatileData(@NonNull LogisticKey key, @NonNull Class<T> type) {
        return Optional.ofNullable((T) persistentBlockData.get(key));
    }

    public void onPlayerBreak(BlockBreakEvent event) {
        // Overwrite to cancel blockbreaking. Uncanceled Blockbreak will remove the LogisticsBlock
    }

    public void onLeftClick(PlayerInteractEvent event) {

    }

    public void onRightClick(PlayerInteractEvent event) {

    }

}
