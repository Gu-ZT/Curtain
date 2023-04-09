package dev.dubhe.curtain.mixins;

import dev.dubhe.curtain.features.player.fakes.IPistonBlock;
import net.minecraft.block.PistonBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PistonBlock.class)
public abstract class PistonBaseBlockMixin implements IPistonBlock {
    @Shadow
    protected abstract boolean getNeighborSignal(World p_60178_, BlockPos p_60179_, Direction p_60180_);

    @Override
    public boolean publicShouldExtend(World world, BlockPos blockPos, Direction direction) {
        return getNeighborSignal(world, blockPos, direction);
    }
}
