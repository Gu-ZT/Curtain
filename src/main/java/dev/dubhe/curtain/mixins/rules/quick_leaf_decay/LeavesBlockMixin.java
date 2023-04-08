package dev.dubhe.curtain.mixins.rules.quick_leaf_decay;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(LeavesBlock.class)
public abstract class LeavesBlockMixin {
    @Shadow
    public abstract void randomTick(BlockState p_221379_, ServerWorld p_221380_, BlockPos p_221381_, Random p_221382_);

    @Inject(method = "tick", at = @At("RETURN"))
    private void tick(BlockState state, ServerWorld level, BlockPos pos, Random random, CallbackInfo ci) {
        if (CurtainRules.quickLeafDecay) {
            this.randomTick(state, level, pos, random);
        }
    }
}
