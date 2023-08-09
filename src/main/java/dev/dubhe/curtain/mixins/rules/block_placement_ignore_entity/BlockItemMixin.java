package dev.dubhe.curtain.mixins.rules.block_placement_ignore_entity;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin {

    @Shadow
    protected abstract boolean mustSurvive();

    @Inject(method = "canPlace", at = @At(value = "HEAD"), cancellable = true)
    private void skipCollisionCheck(BlockItemUseContext pContext, BlockState pState, CallbackInfoReturnable<Boolean> cir) {
        if (CurtainRules.blockPlacementIgnoreEntity) {
            PlayerEntity player = pContext.getPlayer();
            if (player != null && player.isCreative()) {
                cir.setReturnValue(!this.mustSurvive() || pState.canSurvive(pContext.getLevel(), pContext.getClickedPos()));
            }
        }
    }
}
