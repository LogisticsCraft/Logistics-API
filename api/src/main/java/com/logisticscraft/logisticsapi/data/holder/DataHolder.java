package com.logisticscraft.logisticsapi.data.holder;

import com.logisticscraft.logisticsapi.data.LogisticKey;
import com.logisticscraft.logisticsapi.persistence.Persistent;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Optional;

@NoArgsConstructor
public class DataHolder {

    @Persistent
    private HashMap<LogisticKey, Object> data = new HashMap<>();

    /**
     * Set the property value with the given key.
     *
     * @param key   the property key
     * @param value the value
     * @param <T>   the value type
     */
    public <T> void set(@NonNull LogisticKey key, T value) {
        data.put(key, value);
    }

    /**
     * Removes the property with the given key.
     *
     * @param key the property key
     */
    public void remove(@NonNull LogisticKey key) {
        data.remove(key);
    }

    /**
     * Get the property value with the given key.
     *
     * @param key  the property key
     * @param type the expected object type
     * @return the saved value
     */
    @SuppressWarnings("unchecked")
    public <T> Optional<T> get(@NonNull LogisticKey key, Class<T> type) {
        return Optional.ofNullable(type.cast(data.get(key)));
    }

}
