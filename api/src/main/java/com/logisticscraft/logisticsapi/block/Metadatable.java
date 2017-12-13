package com.logisticscraft.logisticsapi.block;

import lombok.NonNull;

import java.util.Optional;

public interface Metadatable {

    <T> void setMetadata(@NonNull String key, T value);
    void removeMetadata(@NonNull String key);
    <T> Optional<T> getMetadata(@NonNull String key, @NonNull Class<T> type);

}
