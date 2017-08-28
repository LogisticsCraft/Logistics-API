package com.logisticscraft.logisticsapi.item;

public enum FilteringMode {
	FILTERBY_TYPE(),
	FILTERBY_TYPE_DAMAGE(),
	FILTERBY_TYPE_NBT(),
	FILTERBY_TYPE_DAMAGE_NBT(),
	BLOCK_ALL(),
	INVERT();

	public int getId() {
		return this.ordinal();
	}

	public static FilteringMode fromId(int id) {
		return FilteringMode.values()[id];
	}

}