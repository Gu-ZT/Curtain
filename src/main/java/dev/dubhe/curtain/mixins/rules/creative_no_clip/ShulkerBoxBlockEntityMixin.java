package dev.dubhe.curtain.mixins.rules.creative_no_clip;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.ShulkerBoxTileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ShulkerBoxTileEntity.class)
public abstract class ShulkerBoxBlockEntityMixin {
    @Redirect(
            method = "moveCollidedEntities",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;getPistonPushReaction()Lnet/minecraft/block/material/PushReaction;"
            )
    )
    private PushReaction getPistonBehaviourOfNoClipPlayers(Entity entity) {
        if (CurtainRules.creativeNoClip && entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            if (player.isCreative() && player.abilities.flying) {
                return PushReaction.IGNORE;
            }
        }
        return entity.getPistonPushReaction();
    }
}
