package com.logisticscraft.logisticsapi.rewrite;

import lombok.NonNull;

import java.util.Optional;

public interface LogisticDataHolder {

    <T> void setLogisticData(@NonNull String key, T value);

    void removeLogisticData(@NonNull String key);

    <T> Optional<T> getLogisticData(@NonNull String key, @NonNull Class<T> type);

}
