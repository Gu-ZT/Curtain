package dev.dubhe.curtain.mixins;

import dev.dubhe.curtain.Curtain;
import dev.dubhe.curtain.utils.CurtainProfiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServer_coreMixin
{
    //to inject right before
    // this.tickWorlds(booleanSupplier_1);
    @Inject(
            method = "tickServer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/MinecraftServer;tickChildren(Ljava/util/function/BooleanSupplier;)V",
                    shift = At.Shift.BEFORE,
                    ordinal = 0
            )
    )
    private void onTick(BooleanSupplier booleanSupplier_1, CallbackInfo ci) {
        CurtainProfiler.ProfilerToken token = CurtainProfiler.start_section(null, "Carpet", CurtainProfiler.TYPE.GENERAL);
        Curtain.tick((MinecraftServer) (Object) this);
        CurtainProfiler.end_current_section(token);
    }

    @Inject(method = "stopServer", at = @At("HEAD"))
    private void serverClosed(CallbackInfo ci)
    {
        Curtain.onServerClosed((MinecraftServer) (Object) this);
    }

    @Shadow
    public abstract ServerLevel overworld();
}
