package dev.dubhe.curtain.mixins.rules.turtle_egg_trampled_disabled;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.entity.ai.goal.BreakBlockGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BreakBlockGoal.class)
public class RemoveBlockGoalMixin {

    @Inject(
            method = "tick", cancellable = true,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/ai/goal/BreakBlockGoal;playDestroyProgressSound(Lnet/minecraft/world/IWorld;Lnet/minecraft/util/math/BlockPos;)V"
            )
    )
    private void dontBreakTheEgg(CallbackInfo ci) {
        if (CurtainRules.turtleEggTrampledDisabled) ci.cancel();
    }
}
