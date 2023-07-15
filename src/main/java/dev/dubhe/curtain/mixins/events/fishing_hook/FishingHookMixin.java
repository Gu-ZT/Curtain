package dev.dubhe.curtain.mixins.events.fishing_hook;

import dev.dubhe.curtain.events.events.FishingHookEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishingHook.class)
public abstract class FishingHookMixin {
    @Inject(method = "catchingFish", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/syncher/SynchedEntityData;set(Lnet/minecraft/network/syncher/EntityDataAccessor;Ljava/lang/Object;)V", ordinal = 1))
    private void catchingFish(BlockPos pos, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new FishingHookEvent.Catching((FishingHook) ((Object) this), pos));
    }
}
