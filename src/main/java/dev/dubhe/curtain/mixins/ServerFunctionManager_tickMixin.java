package dev.dubhe.curtain.mixins;

import dev.dubhe.curtain.features.rules.fakes.MinecraftServerInterface;
import dev.dubhe.curtain.utils.CurtainProfiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerFunctionManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerFunctionManager.class)
public class ServerFunctionManager_tickMixin
{
    @Shadow @Final MinecraftServer server;
    CurtainProfiler.ProfilerToken currentSection;

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void beforeDatapacks(CallbackInfo ci)
    {
        if (! ((MinecraftServerInterface)server).getTickRateManager().runsNormally())
        {
            ci.cancel();
        }
        else
        {
            currentSection = CurtainProfiler.start_section(null, "Datapacks", CurtainProfiler.TYPE.GENERAL);
        }
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void afterDatapacks(CallbackInfo ci)
    {
        CurtainProfiler.end_current_section(currentSection);
    }
}
