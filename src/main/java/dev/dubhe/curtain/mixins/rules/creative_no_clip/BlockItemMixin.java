package dev.dubhe.curtain.mixins.rules.creative_no_clip;

import dev.dubhe.curtain.CurtainRules;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin {
    @Redirect(
            method = "canPlace",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;isUnobstructed(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/shapes/ISelectionContext;)Z"
            )
    )
    private boolean canSpectatingPlace(World world, BlockState state, BlockPos pos, ISelectionContext context,
                                       BlockItemUseContext contextOuter, BlockState stateOuter) {
        PlayerEntity player = contextOuter.getPlayer();
        if (CurtainRules.creativeNoClip && player != null && player.isCreative() && player.abilities.flying) {
            // copy from canPlace
            VoxelShape voxelShape = state.getCollisionShape(world, pos, context);
            return voxelShape.isEmpty() || world.isUnobstructed(player, voxelShape.move(pos.getX(), pos.getY(), pos.getZ()));

        }
        return world.isUnobstructed(state, pos, context);
    }
}
