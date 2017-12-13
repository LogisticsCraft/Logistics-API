package com.logisticscraft.logisticsapi.rewrite.block;

import com.logisticscraft.logisticsapi.rewrite.data.LogisticDataHolder;
import com.logisticscraft.logisticsapi.rewrite.data.LogisticKey;
import com.logisticscraft.logisticsapi.rewrite.persistence.Persistent;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Optional;

@NoArgsConstructor
public abstract class LogisticBlock implements LogisticDataHolder {

    @Persistent
    private HashMap<LogisticKey, Object> data = new HashMap<>();

    @Override
    public <T> void setLogisticData(@NonNull LogisticKey key, T value) {
        data.put(key, value);
    }

    @Override
    public void removeLogisticData(@NonNull LogisticKey key) {
        data.remove(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> getLogisticData(@NonNull LogisticKey key, @NonNull Class<T> type) {
        return Optional.ofNullable((T) data.get(key));
    }

}
