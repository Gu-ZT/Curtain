package dev.dubhe.curtain.mixins.rules.quick_leaf_decay;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LeavesBlock.class)
public abstract class LeavesBlockMixin {
    @Shadow
    public abstract void randomTick(BlockState p_221379_, ServerLevel p_221380_, BlockPos p_221381_, RandomSource p_221382_);

    @Inject(method = "tick", at = @At("RETURN"))
    private void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if (CurtainRules.quickLeafDecay) {
            this.randomTick(state, level, pos, random);
        }
    }
}
