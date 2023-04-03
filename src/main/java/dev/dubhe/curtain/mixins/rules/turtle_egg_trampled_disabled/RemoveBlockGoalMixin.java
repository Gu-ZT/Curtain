package dev.dubhe.curtain.mixins.rules.turtle_egg_trampled_disabled;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.world.entity.ai.goal.RemoveBlockGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RemoveBlockGoal.class)
public class RemoveBlockGoalMixin {

    @Inject(method = "tick", cancellable = true, at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/ai/goal/RemoveBlockGoal;playDestroyProgressSound(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;)V"
    ))
    private void dontBreakTheEgg(CallbackInfo ci) {
        if (CurtainRules.turtleEggTrampledDisabled) ci.cancel();
    }
}
