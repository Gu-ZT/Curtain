package dev.dubhe.curtain.mixins.rules.creative_no_clip;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TheEndGatewayBlockEntity.class)
public abstract class TheEndGatewayBlockEntityMixin {
    @Inject(method = "canEntityTeleport", cancellable = true, at = @At("HEAD"))
    private static void checkFlyingCreative(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (CurtainRules.isCreativeFlying(entity)) {
            cir.setReturnValue(false);
        }
    }
}
