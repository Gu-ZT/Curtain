package dev.dubhe.curtain.mixins.rules.scaffolding_distance;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.world.level.block.ScaffoldingBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ScaffoldingBlock.class)
public abstract class ScaffoldingBlockMixin {
    @ModifyConstant(method = "tick",constant = @Constant(intValue = 7),require = 0)
    private int tick(int oldValue){
        return CurtainRules.scaffoldingDistance;
    }

    @ModifyConstant(method = "canSurvive",constant = @Constant(intValue = 7),require = 0)
    private int canSurvive(int oldValue){
        return CurtainRules.scaffoldingDistance;
    }
}
