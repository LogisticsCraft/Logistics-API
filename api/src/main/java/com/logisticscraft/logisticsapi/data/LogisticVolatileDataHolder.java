package com.logisticscraft.logisticsapi.data;

import lombok.NonNull;

import java.util.Optional;

public interface LogisticVolatileDataHolder {

    <T> void setLogisticVolatileData(@NonNull LogisticKey key, T value);

    void removeLogisticVolatileData(@NonNull LogisticKey key);

    <T> Optional<T> getLogisticVolatileData(@NonNull LogisticKey key, @NonNull Class<T> type);

}
