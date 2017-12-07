package com.logisticscraft.logisticsapi.event;

import org.bukkit.Location;
import com.logisticscraft.logisticsapi.item.ItemContainer;

public class ItemContainerRegisterEvent extends LogisticBlockRegisterEvent {

	private final ItemContainer itemcontainer;

	public ItemContainerRegisterEvent(Location location, ItemContainer object) {
        super(location, object);
        this.itemcontainer = object;
    }

    public ItemContainer getItemContainer() {
        return itemcontainer;
    }

}
