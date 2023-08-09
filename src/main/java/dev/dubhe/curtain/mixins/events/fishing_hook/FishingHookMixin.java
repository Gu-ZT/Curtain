package dev.dubhe.curtain.mixins.events.fishing_hook;

import dev.dubhe.curtain.events.events.FishingHookEvent;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishingBobberEntity.class)
public abstract class FishingHookMixin {
    @Inject(method = "catchingFish", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/datasync/EntityDataManager;set(Lnet/minecraft/network/datasync/DataParameter;Ljava/lang/Object;)V", ordinal = 1))
    private void catchingFish(BlockPos pPos, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new FishingHookEvent.Catching((FishingBobberEntity) ((Object) this), pPos));
    }
}
