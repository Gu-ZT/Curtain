package dev.dubhe.curtain.mixins.rules.turtle_egg_trampled_disabled;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.block.TurtleEggBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TurtleEggBlock.class)
public class TurtleEggBlockMixin {
    @Inject(method = "destroyEgg", cancellable = true,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/TurtleEggBlock;decreaseEggs(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V"
            )
    )
    private void dontBreakTheEgg(World p_203167_1_, BlockPos p_203167_2_, Entity p_203167_3_, int p_203167_4_, CallbackInfo ci) {
        if (CurtainRules.turtleEggTrampledDisabled) ci.cancel();
    }
}
