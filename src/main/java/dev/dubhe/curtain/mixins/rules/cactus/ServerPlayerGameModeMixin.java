package dev.dubhe.curtain.mixins.rules.cactus;

import dev.dubhe.curtain.utils.BlockRotator;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerInteractionManager.class)
public abstract class ServerPlayerGameModeMixin {
    @Redirect(
            method = "useItemOn",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockState;use(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/util/math/BlockRayTraceResult;)Lnet/minecraft/util/ActionResultType;"
            )
    )
    private ActionResultType activateWithOptionalCactus(BlockState blockState, World world, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        boolean flipped = BlockRotator.flipBlockWithCactus(blockState, world, player, hand, result);
        if (flipped) {
            return ActionResultType.SUCCESS;
        }
        return blockState.use(world, player, hand, result);
    }
}
