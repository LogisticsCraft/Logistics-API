package com.logisticscraft.logisticsapi.event;

import org.bukkit.Location;
import com.logisticscraft.logisticsapi.item.ItemContainer;

public class ItemContainerUnregisterEvent extends LogisticBlockUnregisterEvent {

	private final ItemContainer itemcontainer;

	public ItemContainerUnregisterEvent(Location location, ItemContainer object) {
        super(location, object);
        this.itemcontainer = object;
    }

    public ItemContainer getItemContainer() {
        return itemcontainer;
    }

}
