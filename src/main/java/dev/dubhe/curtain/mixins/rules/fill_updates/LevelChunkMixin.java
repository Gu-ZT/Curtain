package dev.dubhe.curtain.mixins.rules.fill_updates;

import dev.dubhe.curtain.CurtainRules;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Chunk.class)
public abstract class LevelChunkMixin {
    @Redirect(
            method = "setBlockState",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockState;onPlace(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Z)V"
            )
    )
    private void onAdded(BlockState blockState, World world, BlockPos blockPos, BlockState state, boolean b) {
        if (!CurtainRules.impendingFillSkipUpdates.get()) {
            blockState.onPlace(world, blockPos, state, b);
        }
    }

    @Redirect(
            method = "setBlockState",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockState;onRemove(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Z)V"
            )
    )
    private void onRemovedBlock(BlockState blockState, World world, BlockPos blockPos, BlockState state, boolean b) {
        if (!CurtainRules.impendingFillSkipUpdates.get()) {
            if (state.hasTileEntity() && !state.is(state.getBlock())) {
                world.removeBlockEntity(blockPos);
            }
        } else {
            state.onRemove(world, blockPos, state, b);
        }
    }

}
