package com.logisticscraft.logisticsapi;

import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

/**
 * @author JARvis (Пётр) PROgrammer
 */
public enum BlockSide {
    EAST(new Vector(1.0, 0.0, 0.0)),
    WEST(new Vector(-1.0, 0.0, 0.0)),
    UP(new Vector(0.0, 1.0, 0.0)),
    DOWN(new Vector(0.0, -1.0, 0.0)),
    SOUTH(new Vector(0.0, 0.0, 1.0)),
    NORTH(new Vector(0.0, 0.0, -1.0));

    private final Vector blockVector;

    BlockSide(Vector blockVector) {
        this.blockVector = blockVector;
    }

    public static BlockSide fromBlockFace(BlockFace blockFace) {
        return valueOf(blockFace.name());
    }

    public static BlockFace toBlockFace(BlockSide blockSide) {
        return BlockFace.valueOf(blockSide.name());
    }

    public BlockFace toBlockFace() {
        return toBlockFace(this);
    }
}
