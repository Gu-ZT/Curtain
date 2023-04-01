package dev.dubhe.curtain.mixins.rules.fill_updates;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LevelChunk.class)
public class LevelChunkMixin {
    @Redirect(
            method = "setBlockState",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;onPlace(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Z)V"
            )
    )
    private void onAdded(BlockState blockState, Level world, BlockPos blockPos, BlockState state, boolean b) {
        if (!CurtainRules.impendingFillSkipUpdates.get()) {
            blockState.onPlace(world, blockPos, state, b);
        }
    }
    @Redirect(
            method = "setBlockState",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;onRemove(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Z)V"
            )
    )
    private void onRemovedBlock(BlockState blockState, Level world, BlockPos blockPos, BlockState state, boolean b) {
        if (!CurtainRules.impendingFillSkipUpdates.get()) {
            if (state.hasBlockEntity() && !state.is(state.getBlock())) {
                world.removeBlockEntity(blockPos);
            }
        } else {
            state.onRemove(world, blockPos, state, b);
        }
    }

}
