package dev.dubhe.curtain.mixins.rules.farmland_trampled_disabled;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FarmlandBlock.class)
public class FarmBlockMixin {
    @Redirect(
            method = "fallOn",
            at = @At(value = "FIELD", target = "Lnet/minecraft/world/World;isClientSide:Z")
    )
    private boolean farmlandTrampledDisabled(World instance) {
        return CurtainRules.farmlandTrampledDisabled || instance.isClientSide;
    }
}
