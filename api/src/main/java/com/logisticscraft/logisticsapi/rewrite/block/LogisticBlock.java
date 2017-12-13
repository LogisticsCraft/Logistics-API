package com.logisticscraft.logisticsapi.rewrite.block;

import com.logisticscraft.logisticsapi.rewrite.Metadatable;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@NoArgsConstructor
public abstract class LogisticBlock implements Metadatable {

    private Map<String, Object> metadata = new HashMap<>();

    @Override
    public <T> void setMetadata(@NonNull String key, T value) {
        metadata.put(key, value);
    }

    @Override
    public void removeMetadata(@NonNull String key) {
        metadata.remove(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> getMetadata(@NonNull String key, @NonNull Class<T> type) {
        return Optional.ofNullable((T) metadata.get(key));
    }

}
