package dev.dubhe.curtain.utils;

import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public class BlockSaplingHelper {
    // Added code for checking water for dead shrub rule
    public static boolean hasWater(IWorld worldIn, BlockPos pos)
    {
        for (BlockPos blockpos$mutableblockpos : BlockPos.betweenClosed(pos.offset(-4, -4, -4), pos.offset(4, 1, 4)))
        {
            if (worldIn.getBlockState(blockpos$mutableblockpos).getMaterial() == Material.WATER)
            {
                return true;
            }
        }

        return false;
    }
}
