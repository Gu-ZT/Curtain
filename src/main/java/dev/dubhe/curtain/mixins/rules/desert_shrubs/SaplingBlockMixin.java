package dev.dubhe.curtain.mixins.rules.desert_shrubs;

import dev.dubhe.curtain.CurtainRules;

import dev.dubhe.curtain.utils.BlockSaplingHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SaplingBlock;
import net.minecraft.tags.FluidTags;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(SaplingBlock.class)
public abstract class SaplingBlockMixin {
    @Inject(method = "advanceTree", at = @At(value = "INVOKE", shift = At.Shift.BEFORE,
            target = "Lnet/minecraft/block/trees/Tree;growTree(Lnet/minecraft/world/server/ServerWorld;Lnet/minecraft/world/gen/ChunkGenerator;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Ljava/util/Random;)Z"),
            cancellable = true)
    private void onGenerate(ServerWorld serverWorld_1, BlockPos blockPos_1, BlockState blockState_1, Random random_1, CallbackInfo ci) {
        if (CurtainRules.desertShrubs && serverWorld_1.getBiome(blockPos_1).getBiomeCategory() == Biome.Category.DESERT && !BlockSaplingHelper.hasWater(serverWorld_1, blockPos_1)) {
            serverWorld_1.setBlock(blockPos_1, Blocks.DEAD_BUSH.defaultBlockState(), 3);
            ci.cancel();
        }
    }
}
