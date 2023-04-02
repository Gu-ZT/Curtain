package dev.dubhe.curtain.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Map.entry;

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
            .collect(Collectors.toUnmodifiableMap(DyeColor::getMaterialColor, Function.identity()));

    /**
     * A map of all the wool colours to their respective colours in the {@link Messenger#m} format so the name of the counter
     * gets printed in colour.
     */

    public static final Map<MaterialColor, String> Material2DyeName = Map.ofEntries(
            entry(MaterialColor.SNOW, "w "),
            entry(MaterialColor.COLOR_ORANGE, "#F9801D "),
            entry(MaterialColor.COLOR_MAGENTA, "m "),
            entry(MaterialColor.COLOR_LIGHT_BLUE, "t "),
            entry(MaterialColor.COLOR_YELLOW, "y "),
            entry(MaterialColor.COLOR_LIGHT_GREEN, "l "),
            entry(MaterialColor.COLOR_PINK, "#FFACCB "),
            entry(MaterialColor.COLOR_GRAY, "f "),
            entry(MaterialColor.COLOR_LIGHT_GRAY, "g "),
            entry(MaterialColor.COLOR_CYAN, "c "),
            entry(MaterialColor.COLOR_PURPLE, "p "),
            entry(MaterialColor.COLOR_BLUE, "v "),
            entry(MaterialColor.COLOR_BROWN, "#835432 "),
            entry(MaterialColor.COLOR_GREEN, "e "),
            entry(MaterialColor.COLOR_RED, "r "),
            entry(MaterialColor.COLOR_BLACK, "k ")
    );

    /**
     * Gets the colour of wool at the position, for hoppers to be able to decide whether to add their items to the global counter.
     */
    public static DyeColor getWoolColorAtPosition(Level worldIn, BlockPos pos) {
        BlockState state = worldIn.getBlockState(pos);
        if (state.getMaterial() != Material.WOOL || !state.isRedstoneConductor(worldIn, pos)) //isSimpleFullBlock
            return null;
        return Material2Dye.get(state.getMapColor(worldIn, pos));
    }
}
