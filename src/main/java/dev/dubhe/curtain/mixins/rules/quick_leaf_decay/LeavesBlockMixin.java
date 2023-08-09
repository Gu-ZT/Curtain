package dev.dubhe.curtain.mixins.rules.quick_leaf_decay;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(LeavesBlock.class)
public abstract class LeavesBlockMixin {
    @Shadow
    public abstract void randomTick(BlockState par1, ServerLevel par2, BlockPos par3, Random par4);

    @Inject(method = "tick", at = @At("RETURN"))
    private void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, Random pRandom, CallbackInfo ci) {
        if (CurtainRules.quickLeafDecay) {
            this.randomTick(pState, pLevel, pPos, pRandom);
        }
    }
}
