package dev.dubhe.curtain.mixins;

import dev.dubhe.curtain.CurtainRules;
import dev.dubhe.curtain.features.player.fakes.IEntity;
import dev.dubhe.curtain.utils.BlockRotator;

import net.minecraft.entity.Entity;
import net.minecraft.util.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Direction.class)
public abstract class DirectionMixin {
    @Redirect(method = "orderedByNearest", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getViewYRot(F)F"))
    private static float getYaw(Entity entity, float f) {
        float yaw;
        if (CurtainRules.placementRotationFix) {
            yaw = entity.getViewYRot(f);
        } else {
            yaw = ((IEntity) entity).getMainYaw(f);
        }
        if (BlockRotator.flippinEligibility(entity)) {
            yaw += 180f;
        }

        return yaw;
    }

    @Redirect(method = "orderedByNearest", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getViewXRot(F)F"))
    private static float getPitch(Entity entity, float f) {
        float pitch = entity.getViewXRot(f);
        if (BlockRotator.flippinEligibility(entity)) {
            pitch = -pitch;
        }
        return pitch;
    }
}
