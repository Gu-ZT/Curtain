package dev.dubhe.curtain.features.player.fakes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public interface IPistonBlock {
    boolean publicShouldExtend(Level world, BlockPos blockPos, Direction direction);
}
