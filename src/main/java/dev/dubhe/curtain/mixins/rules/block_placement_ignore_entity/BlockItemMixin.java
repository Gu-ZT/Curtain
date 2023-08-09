package dev.dubhe.curtain.mixins.rules.block_placement_ignore_entity;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
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
    private void skipCollisionCheck(BlockPlaceContext context, BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (CurtainRules.blockPlacementIgnoreEntity) {
            Player player = context.getPlayer();
            if (player != null && player.isCreative()) {
                cir.setReturnValue(!this.mustSurvive() || state.canSurvive(context.getLevel(), context.getClickedPos()));
            }
        }
    }
}
