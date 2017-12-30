package com.logisticscraft.logisticsapi.data;

import lombok.NonNull;

import java.util.Optional;

public interface LogisticPersistentDataHolder {

    <T> void setLogisticPersistentData(@NonNull LogisticKey key, T value);

    void removeLogisticPersistentData(@NonNull LogisticKey key);

    <T> Optional<T> getLogisticPersistentData(@NonNull LogisticKey key, @NonNull Class<T> type);

}
