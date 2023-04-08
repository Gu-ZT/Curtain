package dev.dubhe.curtain.mixins.rules.super_lead;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.INPC;
import net.minecraft.entity.merchant.IMerchant;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractVillagerEntity.class)
public abstract class AbstractVillagerMixin extends AgeableEntity implements INPC, IMerchant {
    protected AbstractVillagerMixin(EntityType<? extends AgeableEntity> type, World level) {
        super(type, level);
    }

    @Inject(method = "canBeLeashed", at = @At("RETURN"), cancellable = true)
    private void canBeLeashed(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        if (CurtainRules.superLead) {
            cir.setReturnValue(!this.isLeashed());
        }
    }
}
