package dev.dubhe.curtain.mixins.rules.scaffolding_distance;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.world.item.ScaffoldingBlockItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ScaffoldingBlockItem.class)
public abstract class ScaffoldingBlockItemMixin {
    @ModifyConstant(method = "updatePlacementContext",constant = @Constant(intValue = 7),require = 0)
    private int updatePlacementContext(int oldValue){
        return CurtainRules.scaffoldingDistance;
    }
}
