package dev.dubhe.curtain.mixins.rules.cactus;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(World.class)
public abstract class LevelMixin {
    @ModifyConstant(
            method = "markAndNotifyBlock", //setBlockState main
            remap = false,
            constant = @Constant(intValue = 16)
    )
    private int addFillUpdatesInt(int original) {
        if (CurtainRules.impendingFillSkipUpdates.get()) {
            return -1;
        }
        return original;
    }

    @Redirect(
            method = "markAndNotifyBlock",
            remap = false,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;blockUpdated(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;)V"
            )
    )
    private void updateNeighborsMaybe(World world, BlockPos blockPos, Block block) {
        if (!CurtainRules.impendingFillSkipUpdates.get()) {
            world.blockUpdated(blockPos, block);
        }
    }
}
