package com.logisticscraft.logisticsapi.data;

import com.logisticscraft.logisticsapi.util.Tracer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
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

    /**
     * Translates Bukkit's BlockFaces to LogisticBlockFaces
     *
     * @param blockFace the BlockFace
     * @return the LogisticBlockFace
     */
    public static LogisticBlockFace getBlockFace(@NonNull BlockFace blockFace) {
        for (LogisticBlockFace lface : values())
            if (lface.getBlockFace() == blockFace) return lface;
        Tracer.warn("Trying to get unknown LogisticBlockFace: " + blockFace);
        return LogisticBlockFace.SELF;
    }
}
