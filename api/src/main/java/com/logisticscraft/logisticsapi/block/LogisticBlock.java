package com.logisticscraft.logisticsapi.block;

import com.logisticscraft.logisticsapi.data.LogisticDataHolder;
import com.logisticscraft.logisticsapi.data.LogisticKey;
import com.logisticscraft.logisticsapi.data.SafeBlockLocation;
import com.logisticscraft.logisticsapi.persistence.Persistent;
import lombok.Getter;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Optional;

public abstract class LogisticBlock implements LogisticDataHolder {

    @Getter
    @Persistent
    private LogisticKey typeId;

    @Getter
    @Persistent
    private SafeBlockLocation location;


    private HashMap<LogisticKey, Object> blockData = new HashMap<>();

    @Override
    public <T> void setLogisticData(@NonNull LogisticKey key, T value) {
        blockData.put(key, value);
    }

    @Override
    public void removeLogisticData(@NonNull LogisticKey key) {
        blockData.remove(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> getLogisticData(@NonNull LogisticKey key, @NonNull Class<T> type) {
        return Optional.ofNullable((T) blockData.get(key));
    }

}
