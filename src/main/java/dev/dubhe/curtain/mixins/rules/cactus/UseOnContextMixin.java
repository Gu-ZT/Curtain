package dev.dubhe.curtain.mixins.rules.cactus;

import dev.dubhe.curtain.utils.BlockRotator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemUseContext.class)
public abstract class UseOnContextMixin {
    @Redirect(
            method = "getHorizontalDirection",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;getDirection()Lnet/minecraft/util/Direction;"
            )
    )
    private Direction getPlayerFacing(PlayerEntity playerEntity) {
        Direction dir = playerEntity.getDirection();
        if (BlockRotator.flippinEligibility(playerEntity)) {
            dir = dir.getOpposite();
        }
        return dir;
    }
}
