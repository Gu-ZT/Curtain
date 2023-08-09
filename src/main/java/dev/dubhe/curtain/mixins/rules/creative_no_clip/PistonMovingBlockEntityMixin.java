package dev.dubhe.curtain.mixins.rules.creative_no_clip;

import dev.dubhe.curtain.CurtainRules;
import dev.dubhe.curtain.features.player.patches.EntityPlayerMPFake;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.PistonTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PistonTileEntity.class)
public abstract class PistonMovingBlockEntityMixin {
    @Shadow
    private BlockState movedState;

    @Shadow
    public abstract Direction getMovementDirection();

    @Inject(method = "moveEntityByPiston", at = @At("HEAD"), cancellable = true)
    private static void dontPushSpectators(Direction direction, Entity entity, double d, Direction direction2, CallbackInfo ci) {
        if (CurtainRules.creativeNoClip && entity instanceof PlayerEntity player && player.isCreative() && player.abilities.flying) {
            ci.cancel();
        }
    }

    @Redirect(method = "moveCollidedEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setDeltaMovement(DDD)V"))
    private void ignoreAccel(Entity entity, double x, double y, double z) {
        if (CurtainRules.creativeNoClip && entity instanceof PlayerEntity player && player.isCreative() && player.abilities.flying) {
            return;
        }
        entity.setDeltaMovement(x, y, z);
    }

    @Redirect(
            method = "moveCollidedEntities",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;getPistonPushReaction()Lnet/minecraft/block/material/PushReaction;"
            )
    )
    private PushReaction moveFakePlayers(Entity entity) {
        if (entity instanceof EntityPlayerMPFake && movedState.is(Blocks.SLIME_BLOCK)) {
            Vector3d vec3d = entity.getDeltaMovement();
            double x = vec3d.x;
            double y = vec3d.y;
            double z = vec3d.z;
            Direction direction = getMovementDirection();
            switch (direction.getAxis()) {
                case X -> x = direction.getStepX();
                case Y -> y = direction.getStepY();
                case Z -> z = direction.getStepZ();
            }

            entity.setDeltaMovement(x, y, z);
        }
        return entity.getPistonPushReaction();
    }
}
