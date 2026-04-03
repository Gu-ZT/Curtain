package dev.dubhe.curtain.mixins;

import dev.dubhe.curtain.utils.CurtainProfiler;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ChunkMap.class)
public class ChunkMap_tickMixin
{
    @Shadow @Final ServerLevel level;
    CurtainProfiler.ProfilerToken currentSection;

    @Inject(method = "tick(Ljava/util/function/BooleanSupplier;)V", at = @At("HEAD"))
    private void startProfilerSection(BooleanSupplier booleanSupplier_1, CallbackInfo ci)
    {
        currentSection = CurtainProfiler.start_section(level, "Unloading", CurtainProfiler.TYPE.GENERAL);
    }

    @Inject(method = "tick(Ljava/util/function/BooleanSupplier;)V", at = @At("RETURN"))
    private void stopProfilerSecion(BooleanSupplier booleanSupplier_1, CallbackInfo ci)
    {
        if (currentSection != null)
        {
            CurtainProfiler.end_current_section(currentSection);
        }
    }
}
