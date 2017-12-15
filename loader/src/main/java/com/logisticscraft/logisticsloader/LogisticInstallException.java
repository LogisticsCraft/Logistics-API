package com.logisticscraft.logisticsloader;

public class LogisticInstallException extends Exception {

    public LogisticInstallException(Throwable cause) {
        super("An exception occurred while downloading and enabling LogisticAPI!", cause);
    }

}
