package com.logisticscraft.logisticsapi.excluded.util.bukkit;

import lombok.AllArgsConstructor;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public enum BlockSide {
    EAST(new Vector(1.0, 0.0, 0.0)),
    WEST(new Vector(-1.0, 0.0, 0.0)),
    UP(new Vector(0.0, 1.0, 0.0)),
    DOWN(new Vector(0.0, -1.0, 0.0)),
    SOUTH(new Vector(0.0, 0.0, 1.0)),
    NORTH(new Vector(0.0, 0.0, -1.0));

    public static final Set<BlockSide> ALL_SIDES = new HashSet<>(Arrays.asList(values()));
    private final Vector blockVector;

    public static BlockSide fromBlockFace(BlockFace blockFace) {
        return valueOf(blockFace.name());
    }

    public static BlockFace toBlockFace(BlockSide blockSide) {
        return BlockFace.valueOf(blockSide.name());
    }

    public BlockFace toBlockFace() {
        return toBlockFace(this);
    }

    public Vector getBlockVectorCopy() {
        return blockVector.clone();
    }
}
