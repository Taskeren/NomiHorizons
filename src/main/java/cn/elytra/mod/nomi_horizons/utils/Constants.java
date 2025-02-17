package cn.elytra.mod.nomi_horizons.utils;

import net.minecraft.util.math.AxisAlignedBB;

public class Constants {

    /**
     * A AABB where it is not a full block but leaves a slightly space (0.075 block) between blocks, allowing player
     * to access other blocks behind this block.
     */
    public static final AxisAlignedBB SLIGHTLY_SHRUNK_BLOCK = new AxisAlignedBB(0.075, 0.0, 0.075, 0.925, 1.0, 0.925);

}
