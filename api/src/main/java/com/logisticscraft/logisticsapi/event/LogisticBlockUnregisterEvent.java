package com.logisticscraft.logisticsapi.event;

import org.bukkit.Location;

import com.logisticscraft.logisticsapi.block.LogisticBlock;

import de.tr7zw.itemnbtapi.NBTCompound;

public class LogisticBlockUnregisterEvent extends LogisticBlockSavingEvent{

    public LogisticBlockUnregisterEvent(Location location, LogisticBlock logisticblock, NBTCompound savedata) {
        super(location, logisticblock, savedata);
    }

}
