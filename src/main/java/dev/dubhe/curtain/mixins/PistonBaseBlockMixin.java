package dev.dubhe.curtain.mixins;

import dev.dubhe.curtain.features.player.fakes.IPistonBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PistonBaseBlock.class)
public abstract class PistonBaseBlockMixin implements IPistonBlock {
    @Shadow
    protected abstract boolean getNeighborSignal(SignalGetter p_60178_, BlockPos p_60179_, Direction p_60180_);

    @Override
    public boolean publicShouldExtend(Level world, BlockPos blockPos, Direction direction) {
        return getNeighborSignal(world, blockPos, direction);
    }
}
