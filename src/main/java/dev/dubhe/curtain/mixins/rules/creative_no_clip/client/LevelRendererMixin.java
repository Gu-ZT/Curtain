package dev.dubhe.curtain.mixins.rules.creative_no_clip.client;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldRenderer.class)
public abstract class LevelRendererMixin {
    @Redirect(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/player/ClientPlayerEntity;isSpectator()Z"))
    private boolean canSeeWorld(ClientPlayerEntity clientPlayerEntity) {
        return clientPlayerEntity.isSpectator() || (CurtainRules.creativeNoClip && clientPlayerEntity.isCreative());
    }
}
