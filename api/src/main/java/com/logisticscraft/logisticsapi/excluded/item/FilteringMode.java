package com.logisticscraft.logisticsapi.excluded.item;

public enum FilteringMode {
    FILTERBY_TYPE(),
    FILTERBY_TYPE_DAMAGE(),
    FILTERBY_TYPE_NBT(),
    FILTERBY_TYPE_DAMAGE_NBT(),
    BLOCK_ALL(),
    INVERT();

    public static FilteringMode fromId(int id) {
        return FilteringMode.values()[id];
    }

    public int getId() {
        return this.ordinal();
    }

}