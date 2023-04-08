package dev.dubhe.curtain.mixins.rules.super_lead;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public abstract class MobMixin {
    @Shadow
    public abstract boolean isLeashed();

    @Inject(method = "canBeLeashed", at = @At("RETURN"), cancellable = true)
    private void canBeLeashed(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        if (CurtainRules.superLead) {
            cir.setReturnValue(!this.isLeashed());
        }
    }
}
