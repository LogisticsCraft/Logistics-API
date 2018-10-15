package com.logisticscraft.logisticsapi.service.shutdown;

import ch.jalu.injector.factory.SingletonStore;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.inject.Inject;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ShutdownListenerService {

    @Inject
    private SingletonStore<ShutdownListener> shutdownListeners;

    public void shutdownComponents() {
        shutdownListeners.retrieveAllOfType().forEach(ShutdownListener::onShutdown);
    }
}
