package dev.dubhe.curtain.mixins.rules.cactus;

import dev.dubhe.curtain.utils.BlockRotator;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayerGameMode.class)
public abstract class ServerPlayerGameModeMixin {
    @Redirect(
            method = "useItemOn",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;use(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/phys/BlockHitResult;)Lnet/minecraft/world/InteractionResult;"
            )
    )
    private InteractionResult activateWithOptionalCactus(BlockState blockState, Level world, Player player, InteractionHand hand, BlockHitResult result) {
        boolean flipped = BlockRotator.flipBlockWithCactus(blockState, world, player, hand, result);
        if (flipped) {
            return InteractionResult.SUCCESS;
        }
        return blockState.use(world, player, hand, result);
    }
}
