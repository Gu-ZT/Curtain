package dev.dubhe.curtain.mixins.rules.cactus;

import dev.dubhe.curtain.utils.BlockRotator;

import net.minecraft.block.HopperBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(HopperBlock.class)
public abstract class HopperBlockMixin {
    @Redirect(
            method = "getStateForPlacement",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/BlockItemUseContext;getClickedFace()Lnet/minecraft/util/Direction;"
            )
    )
    private Direction getOppositeOpposite(BlockItemUseContext context) {
        if (BlockRotator.flippinEligibility(context.getPlayer())) {
            return context.getClickedFace().getOpposite();
        }
        return context.getClickedFace();
    }
}
