package com.logisticscraft.logisticsapi.data;

import lombok.NonNull;

import java.util.Optional;

public interface VolatileLogisticDataHolder {

    <T> void setVolatileLogisticData(@NonNull LogisticKey key, T value);

    void removeVolatileLogisticData(@NonNull LogisticKey key);

    <T> Optional<T> getVolatileLogisticData(@NonNull LogisticKey key, @NonNull Class<T> type);

}
