package com.logisticscraft.logisticsapi.block;

public abstract class LogisticBlock implements PowerHolder {

    private long power = 0;

    @Override
    public long getPower() {
        return power;
    }

    @Override
    public void setPower(long power) {
        this.power = power;
    }

}
