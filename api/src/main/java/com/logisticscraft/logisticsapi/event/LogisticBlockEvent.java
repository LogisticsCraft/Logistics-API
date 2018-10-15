package com.logisticscraft.logisticsapi.event;

import com.logisticscraft.logisticsapi.block.LogisticBlock;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.event.Event;

@Getter
@RequiredArgsConstructor
public abstract class LogisticBlockEvent extends Event {

    @NonNull
    private final Location location;
    @NonNull
    private final LogisticBlock logisticBlock;
}
