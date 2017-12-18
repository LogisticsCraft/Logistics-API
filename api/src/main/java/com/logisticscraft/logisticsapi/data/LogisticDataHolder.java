package com.logisticscraft.logisticsapi.data;

import lombok.NonNull;

import java.util.Optional;

public interface LogisticDataHolder {

    <T> void setLogisticData(@NonNull LogisticKey key, T value);

    void removeLogisticData(@NonNull LogisticKey key);

    <T> Optional<T> getLogisticData(@NonNull LogisticKey key, @NonNull Class<T> type);

}
