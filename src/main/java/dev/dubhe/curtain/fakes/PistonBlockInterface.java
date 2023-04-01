package dev.dubhe.curtain.fakes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public interface PistonBlockInterface {
    boolean publicShouldExtend(Level world, BlockPos blockPos, Direction direction);
}
