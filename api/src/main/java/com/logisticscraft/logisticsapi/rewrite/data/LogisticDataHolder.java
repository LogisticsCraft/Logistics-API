package com.logisticscraft.logisticsapi.rewrite.data;

import com.logisticscraft.logisticsapi.rewrite.data.LogisticKey;
import lombok.NonNull;

import java.util.Optional;

public interface LogisticDataHolder {

    <T> void setLogisticData(@NonNull LogisticKey key, T value);

    void removeLogisticData(@NonNull LogisticKey key);

    <T> Optional<T> getLogisticData(@NonNull LogisticKey key, @NonNull Class<T> type);

}
