package com.logisticscraft.logisticsapi.rewrite.event;

import com.logisticscraft.logisticsapi.rewrite.block.LogisticBlock;
import com.logisticscraft.logisticsapi.rewrite.data.SafeBlockLocation;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;

@Getter
@RequiredArgsConstructor
public abstract class LogisticBlockEvent extends Event {

    @NonNull
    private final SafeBlockLocation safeBlockLocation;
    @NonNull
    private final LogisticBlock logisticBlock;

}
