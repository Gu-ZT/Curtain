package dev.dubhe.curtain.mixins.rules.cactus;

import dev.dubhe.curtain.utils.BlockRotator;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(UseOnContext.class)
public abstract class UseOnContextMixin {
    @Redirect(
            method = "getHorizontalDirection",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getDirection()Lnet/minecraft/core/Direction;"
            )
    )
    private Direction getPlayerFacing(Player playerEntity) {
        Direction dir = playerEntity.getDirection();
        if (BlockRotator.flippinEligibility(playerEntity)) {
            dir = dir.getOpposite();
        }
        return dir;
    }
}
