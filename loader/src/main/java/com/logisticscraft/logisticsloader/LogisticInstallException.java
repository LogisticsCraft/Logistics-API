package com.logisticscraft.logisticsloader;

import lombok.NonNull;

public class LogisticInstallException extends Exception {

    public LogisticInstallException(@NonNull Throwable cause) {
        super("An exception occurred while downloading and enabling LogisticAPI!", cause);
    }

}
