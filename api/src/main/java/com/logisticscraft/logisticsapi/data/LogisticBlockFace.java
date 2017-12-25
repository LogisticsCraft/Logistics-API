package com.logisticscraft.logisticsapi.data;

import com.logisticscraft.logisticsapi.utils.Tracer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.val;
import org.bukkit.block.BlockFace;

@AllArgsConstructor
public enum LogisticBlockFace {
    TOP(BlockFace.UP),
    BOTTOM(BlockFace.DOWN),
    NORTH(BlockFace.NORTH),
    SOUTH(BlockFace.SOUTH),
    EAST(BlockFace.EAST),
    WEST(BlockFace.WEST),
    SELF(BlockFace.SELF);

    @Getter
    private BlockFace blockFace;

    public static LogisticBlockFace getBlockFace(@NonNull BlockFace blockFace) {
        for (val currentFace : values()) {
            if (currentFace.getBlockFace() == blockFace) {
                return currentFace;
            }
        }
        Tracer.warn("Trying to get unknown LogisticBlockFace: " + blockFace);
        return LogisticBlockFace.SELF;
    }

}
