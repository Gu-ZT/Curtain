package dev.dubhe.curtain.mixins.rules.command_player;

import dev.dubhe.curtain.features.player.patches.EntityPlayerMPFake;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow
    public Level level;

    @Shadow
    @Nullable
    public abstract Entity getControllingPassenger();

    @Inject(method = "isControlledByLocalInstance", at = @At("HEAD"), cancellable = true)
    private void isFakePlayer(CallbackInfoReturnable<Boolean> cir) {
        if (getControllingPassenger() instanceof EntityPlayerMPFake) cir.setReturnValue(!level.isClientSide);
    }
}
