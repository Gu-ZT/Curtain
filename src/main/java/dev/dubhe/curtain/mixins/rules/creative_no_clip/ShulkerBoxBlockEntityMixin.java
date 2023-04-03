package dev.dubhe.curtain.mixins.rules.creative_no_clip;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.material.PushReaction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ShulkerBoxBlockEntity.class)
public abstract class ShulkerBoxBlockEntityMixin {
    @Redirect(
            method = "moveCollidedEntities",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;getPistonPushReaction()Lnet/minecraft/world/level/material/PushReaction;"
            )
    )
    private PushReaction getPistonBehaviourOfNoClipPlayers(Entity entity) {
        if (CurtainRules.creativeNoClip && entity instanceof Player player && player.isCreative() && player.getAbilities().flying) {
            return PushReaction.IGNORE;
        }
        return entity.getPistonPushReaction();
    }
}
