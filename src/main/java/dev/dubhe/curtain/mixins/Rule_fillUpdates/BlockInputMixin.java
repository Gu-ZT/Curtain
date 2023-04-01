package dev.dubhe.curtain.mixins.Rule_fillUpdates;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.commands.arguments.blocks.BlockInput;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockInput.class)
public class BlockInputMixin {
    @Redirect(
            method = "place",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/Block;updateFromNeighbourShapes(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"
            )
    )
    private BlockState postProcessStateProxy(BlockState state, LevelAccessor serverWorld, BlockPos blockPos) {
        if (CurtainRules.impendingFillSkipUpdates.get()) {
            return state;
        }

        return Block.updateFromNeighbourShapes(state, serverWorld, blockPos);
    }
}
