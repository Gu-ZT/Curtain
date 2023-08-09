package dev.dubhe.curtain.mixins.rules.better_sign_interaction;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.block.AbstractSignBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallSignBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractSignBlock.class)
public abstract class SignBlockMixin {
    private final AbstractSignBlock self = (AbstractSignBlock) (Object) this;

    @Inject(method = "use", at = @At(value = "HEAD"), cancellable = true)
    public void use(BlockState pState, World pLevel, BlockPos pPos, PlayerEntity pPlayer, Hand pHand, BlockRayTraceResult pHit, CallbackInfoReturnable<ActionResultType> cir) {
        if (CurtainRules.betterSignInteraction && self instanceof WallSignBlock) {
            Direction direction = pState.getValue(WallSignBlock.FACING);
            BlockPos blockPos = pPos.relative(direction, -1);
            BlockState blockState = pLevel.getBlockState(blockPos);
            if (blockState.getBlock() instanceof WallSignBlock) return;
            BlockRayTraceResult hitResult = new  BlockRayTraceResult(Vector3d.atCenterOf(blockPos), direction, blockPos, false);
            blockState.use(pLevel, pPlayer, pHand, hitResult);
            cir.setReturnValue(ActionResultType.SUCCESS);
        }
    }
}
