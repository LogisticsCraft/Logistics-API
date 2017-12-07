package com.logisticscraft.logisticsapi.registry;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class LogisticObjectData<T extends LogisticObject> {

    private Class<T> object;
    private LogisticsNamespaceKey namespaceKey;
    private LogisticObjectMeta meta;

    public LogisticObjectData(@NonNull Class<T> object, @NonNull LogisticsNamespaceKey namespaceKey) {
        this.object = object;
        this.namespaceKey = namespaceKey;
        if (object.isAnnotationPresent(LogisticObjectMeta.class)) {
            this.meta = object.getAnnotation(LogisticObjectMeta.class);
        }
    }

}
