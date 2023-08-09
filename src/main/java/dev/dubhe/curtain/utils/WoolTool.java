package dev.dubhe.curtain.utils;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.DyeColor;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

/**
 * A series of utility functions and variables for dealing predominantly with hopper counters and determining which counter
 * to add their items to, as well as helping dealing with carpet functionality.
 */
public class WoolTool {
    /**
     * A map of the {@link MaterialColor} to the {@link DyeColor} which is used in {@link WoolTool#getWoolColorAtPosition}
     * to get the colour of wool at a position.
     */
    private static final Map<MaterialColor, DyeColor> Material2Dye = new HashMap<>();

    /**
     * A map of all the wool colours to their respective colours in the {@link Messenger#m} format so the name of the counter
     * gets printed in colour.
     */

    public static final Map<MaterialColor, String> Material2DyeName = new HashMap<>();

    static {
        for (DyeColor color : DyeColor.values()) {
            Material2Dye.put(color.getMaterialColor(),color);
        }
        Material2DyeName.put(MaterialColor.SNOW, "w ");
        Material2DyeName.put(MaterialColor.COLOR_ORANGE, "#F9801D ");
        Material2DyeName.put(MaterialColor.COLOR_MAGENTA, "m ");
        Material2DyeName.put(MaterialColor.COLOR_LIGHT_BLUE, "t ");
        Material2DyeName.put(MaterialColor.COLOR_YELLOW, "y ");
        Material2DyeName.put(MaterialColor.COLOR_LIGHT_GREEN, "l ");
        Material2DyeName.put(MaterialColor.COLOR_PINK, "#FFACCB ");
        Material2DyeName.put(MaterialColor.COLOR_GRAY, "f ");
        Material2DyeName.put(MaterialColor.COLOR_LIGHT_GRAY, "g ");
        Material2DyeName.put(MaterialColor.COLOR_CYAN, "c ");
        Material2DyeName.put(MaterialColor.COLOR_PURPLE, "p ");
        Material2DyeName.put(MaterialColor.COLOR_BLUE, "v ");
        Material2DyeName.put(MaterialColor.COLOR_BROWN, "#835432 ");
        Material2DyeName.put(MaterialColor.COLOR_GREEN, "e ");
        Material2DyeName.put(MaterialColor.COLOR_RED, "r ");
        Material2DyeName.put(MaterialColor.COLOR_BLACK, "k ");
    }

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
