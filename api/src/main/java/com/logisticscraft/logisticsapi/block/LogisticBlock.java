package com.logisticscraft.logisticsapi.block;

public abstract class LogisticBlock implements BlockSelector {

    private static Integer COUNTER = 0;
    private final int id;
    
    public LogisticBlock() {
        synchronized (COUNTER) {
            id = ++COUNTER;
        }
    }
    
    @Override
    public int getId() {
        return id;
    }
    
}
