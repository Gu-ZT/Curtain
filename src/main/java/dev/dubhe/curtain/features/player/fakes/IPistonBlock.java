package dev.dubhe.curtain.features.player.fakes;


import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IPistonBlock {
    boolean publicShouldExtend(World world, BlockPos blockPos, Direction direction);
}
