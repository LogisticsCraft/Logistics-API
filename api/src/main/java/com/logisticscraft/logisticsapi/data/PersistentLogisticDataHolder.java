package com.logisticscraft.logisticsapi.data;

import lombok.NonNull;

import java.util.Optional;

public interface PersistentLogisticDataHolder {

    <T> void setPersistentLogisticData(@NonNull LogisticKey key, T value);

    void removePersistentLogisticData(@NonNull LogisticKey key);

    <T> Optional<T> getPersistentLogisticData(@NonNull LogisticKey key, @NonNull Class<T> type);

}
