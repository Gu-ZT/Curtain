package dev.dubhe.curtain.mixins.rules.farmland_trampled_disabled;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FarmBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FarmBlock.class)
public class FarmBlockMixin {
    @Redirect(
            method = "fallOn",
            at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/Level;isClientSide:Z")
    )
    private boolean farmlandTrampledDisabled(Level instance) {
        return CurtainRules.farmlandTrampledDisabled || instance.isClientSide;
    }
}
