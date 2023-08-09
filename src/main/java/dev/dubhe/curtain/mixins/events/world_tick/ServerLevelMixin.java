package dev.dubhe.curtain.mixins.events.world_tick;

import dev.dubhe.curtain.events.WorldTickEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(BooleanSupplier pHasTimeLeft, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new WorldTickEvent((ServerLevel) (Object) this));
    }
}
