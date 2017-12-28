package com.logisticscraft.logisticsapi.block;

import com.logisticscraft.logisticsapi.data.LogisticDataHolder;
import com.logisticscraft.logisticsapi.data.LogisticKey;
import com.logisticscraft.logisticsapi.data.SafeBlockLocation;
import com.logisticscraft.logisticsapi.persistence.Persistent;
import lombok.Getter;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Optional;

/**
 * Represents a custom block handled by the API.
 */
public abstract class LogisticBlock implements LogisticDataHolder {

    @Getter
    @Persistent
    private LogisticKey typeId;

    @Getter
    @Persistent
    private SafeBlockLocation location;

    private HashMap<LogisticKey, Object> blockData = new HashMap<>();

    /**
     * Set the block property value with the given key.
     *
     * @param key   the property key
     * @param value the value
     * @param <T>   the value type
     */
    @Override
    public <T> void setLogisticData(@NonNull LogisticKey key, T value) {
        blockData.put(key, value);
    }

    /**
     * Removes the block property with the given key.
     *
     * @param key the property key
     */
    @Override
    public void removeLogisticData(@NonNull LogisticKey key) {
        blockData.remove(key);
    }

    /**
     * Get the block property value with the given key.
     *
     * @param key  the property key
     * @param type the expected value type
     * @return the saved value
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> getLogisticData(@NonNull LogisticKey key, @NonNull Class<T> type) {
        return Optional.ofNullable((T) blockData.get(key));
    }

}
