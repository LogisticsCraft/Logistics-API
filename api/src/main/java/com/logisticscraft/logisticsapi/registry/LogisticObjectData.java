package com.logisticscraft.logisticsapi.registry;

import javax.annotation.Nonnull;

/**
 * @author JARvis (Пётр) PROgrammer
 */
public class LogisticObjectData<T extends LogisticObject> {
    private Class<T> object;
    private LogisticsNamespaceKey namespaceKey;
    private LogisticObjectMeta meta;

    public LogisticObjectData(@Nonnull Class<T> object, @Nonnull LogisticsNamespaceKey namespaceKey) {
        this.object = object;
        this.namespaceKey = namespaceKey;
        if (object.isAnnotationPresent(LogisticObjectMeta.class)) {
            this.meta = object.getAnnotation(LogisticObjectMeta.class);
        }

    }

    public Class<T> getObject() {
        return this.object;
    }

    public LogisticsNamespaceKey getNamespaceKey() {
        return this.namespaceKey;
    }

    public LogisticObjectMeta getMeta() {
        return this.meta;
    }
}