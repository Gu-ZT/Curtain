package dev.dubhe.curtain.mixins;

import dev.dubhe.curtain.features.logging.fakes.SpawnGroupInterface;
import dev.dubhe.curtain.utils.SpawnReporter;
import net.minecraft.world.entity.MobCategory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobCategory.class)
public abstract class MobCategoryMixin implements SpawnGroupInterface {
    @Shadow
    @Final
    private int max;

    @Inject(method = "getMaxInstancesPerChunk", at = @At("HEAD"), cancellable = true)
    private void getModifiedCapacity(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue((int) ((double) max * (Math.pow(2.0, (SpawnReporter.mobcap_exponent / 4)))));
    }

    @Override
    public int getInitialSpawnCap() {
        return max;
    }
}
