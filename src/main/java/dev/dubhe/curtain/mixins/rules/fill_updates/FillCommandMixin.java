package dev.dubhe.curtain.mixins.rules.fill_updates;

import dev.dubhe.curtain.CurtainRules;

import net.minecraft.block.Block;
import net.minecraft.command.impl.FillCommand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FillCommand.class)
public abstract class FillCommandMixin {
    @Redirect(
            method = "fillBlocks",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/server/ServerWorld;blockUpdated(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;)V"
            )
    )
    private static void conditionalUpdating(ServerWorld serverWorld, BlockPos blockPos, Block block) {
        if (CurtainRules.fillUpdates) {
            serverWorld.blockUpdated(blockPos, block);
        }
    }
}
