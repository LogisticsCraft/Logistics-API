package com.logisticscraft.logisticsapi.service.shutdown;

import ch.jalu.injector.factory.SingletonStore;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.inject.Inject;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ShutdownHandlerService {

    @Inject
    private SingletonStore<ShutdownHandler> shutdownHandlers;

    public void shutdownComponents() {
        shutdownHandlers.retrieveAllOfType().forEach(ShutdownHandler::handleShutdown);
    }

}
