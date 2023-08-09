package dev.dubhe.curtain.utils;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.DyeColor;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A series of utility functions and variables for dealing predominantly with hopper counters and determining which counter
 * to add their items to, as well as helping dealing with carpet functionality.
 */
public class WoolTool {
    /**
     * A map of the {@link MaterialColor} to the {@link DyeColor} which is used in {@link WoolTool#getWoolColorAtPosition}
     * to get the colour of wool at a position.
     */
    private static final Map<MaterialColor, DyeColor> Material2Dye = Arrays.stream(DyeColor.values())
            .collect(Collectors.toMap(DyeColor::getMaterialColor, Function.identity()));

    /**
     * Gets the colour of wool at the position, for hoppers to be able to decide whether to add their items to the global counter.
     */
    public static DyeColor getWoolColorAtPosition(World worldIn, BlockPos pos) {
        BlockState state = worldIn.getBlockState(pos);
        if (state.is(BlockTags.WOOL) || !state.isRedstoneConductor(worldIn, pos)) //isSimpleFullBlock
            return null;
        return Material2Dye.get(state.getMapColor(worldIn, pos));
    }
}
