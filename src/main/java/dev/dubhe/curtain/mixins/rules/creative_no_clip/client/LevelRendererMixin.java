package dev.dubhe.curtain.mixins.rules.creative_no_clip.client;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
    @Redirect(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isSpectator()Z"))
    private boolean canSeeWorld(LocalPlayer clientPlayerEntity) {
        return clientPlayerEntity.isSpectator() || (CurtainRules.creativeNoClip && clientPlayerEntity.isCreative());
    }
}
