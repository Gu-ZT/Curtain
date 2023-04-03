package dev.dubhe.curtain.mixins;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PrimedTnt.class)
public abstract class PrimedTntMixin extends Entity {
    public PrimedTntMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Inject(method = "<init>(Lnet/minecraft/world/level/Level;DDDLnet/minecraft/world/entity/LivingEntity;)V", at = @At(value = "RETURN"))
    private void initTNTLogger(Level world, double x, double y, double z, LivingEntity entity, CallbackInfo ci) {
        if (CurtainRules.tntPrimerMomentumRemoved) {
            this.setDeltaMovement(0, 0.20000000298023224D, 0);
        }
    }
}
