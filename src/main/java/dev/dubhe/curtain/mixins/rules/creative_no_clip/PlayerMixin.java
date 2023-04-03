package dev.dubhe.curtain.mixins.rules.creative_no_clip;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

    protected PlayerMixin(EntityType<? extends LivingEntity> type, Level world) {
        super(type, world);
    }

    @Redirect(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;isSpectator()Z"
            )
    )
    private boolean canClipTroughWorld(Player playerEntity) {
        return playerEntity.isSpectator()
                || (CurtainRules.creativeNoClip && playerEntity.isCreative() && playerEntity.getAbilities().flying);
    }

    @Redirect(method = "aiStep",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;isSpectator()Z"
            )
    )
    private boolean collidesWithEntities(Player playerEntity) {
        return playerEntity.isSpectator()
                || (CurtainRules.creativeNoClip && playerEntity.isCreative() && playerEntity.getAbilities().flying);
    }

    @Redirect(
            method = "updatePlayerPose",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;isSpectator()Z"
            )
    )
    private boolean spectatorsDontPose(Player playerEntity) {
        return playerEntity.isSpectator()
                || (CurtainRules.creativeNoClip && playerEntity.isCreative() && playerEntity.getAbilities().flying);
    }
}
