package dev.dubhe.curtain.mixins.rules.creative_no_clip;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntity.class)
public abstract class PlayerMixin extends LivingEntity {

    protected PlayerMixin(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }

    @Redirect(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;isSpectator()Z"
            )
    )
    private boolean canClipTroughWorld(PlayerEntity playerEntity) {
        return playerEntity.isSpectator()
                || (CurtainRules.creativeNoClip && playerEntity.isCreative() && playerEntity.abilities.flying);
    }

    @Redirect(method = "aiStep",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;isSpectator()Z"
            )
    )
    private boolean collidesWithEntities(PlayerEntity playerEntity) {
        return playerEntity.isSpectator()
                || (CurtainRules.creativeNoClip && playerEntity.isCreative() && playerEntity.abilities.flying);
    }

    @Redirect(
            method = "updatePlayerPose",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;isSpectator()Z"
            )
    )
    private boolean spectatorsDontPose(PlayerEntity playerEntity) {
        return playerEntity.isSpectator()
                || (CurtainRules.creativeNoClip && playerEntity.isCreative() && playerEntity.abilities.flying);
    }
}
