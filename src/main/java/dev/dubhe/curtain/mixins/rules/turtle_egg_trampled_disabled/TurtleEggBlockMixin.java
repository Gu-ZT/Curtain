package dev.dubhe.curtain.mixins.rules.turtle_egg_trampled_disabled;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TurtleEggBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TurtleEggBlock.class)
public class TurtleEggBlockMixin {
    @Inject(method = "destroyEgg", cancellable = true, at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/TurtleEggBlock;decreaseEggs(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V"
    ))
    private void dontBreakTheEgg(Level p_154851_, BlockState p_154852_, BlockPos p_154853_, Entity p_154854_, int p_154855_, CallbackInfo ci) {
        if (CurtainRules.turtleEggTrampledDisabled) ci.cancel();
    }
}
