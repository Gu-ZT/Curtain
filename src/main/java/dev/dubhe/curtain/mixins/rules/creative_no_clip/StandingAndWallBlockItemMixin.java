package dev.dubhe.curtain.mixins.rules.creative_no_clip;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.WallOrFloorItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IWorldReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WallOrFloorItem.class)
public abstract class StandingAndWallBlockItemMixin {
    @Redirect(
            method = "getPlacementState",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/IWorldReader;isUnobstructed(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/shapes/ISelectionContext;)Z"
            )
    )
    private boolean canCreativePlayerPlace(IWorldReader worldView, BlockState state, BlockPos pos, ISelectionContext context, BlockItemUseContext itemContext) {
        PlayerEntity player = itemContext.getPlayer();
        if (CurtainRules.creativeNoClip && player != null && player.isCreative() && player.abilities.flying) {
            VoxelShape voxelShape = state.getCollisionShape(worldView, pos, context);
            return voxelShape.isEmpty() || worldView.isUnobstructed(player, voxelShape.move(pos.getX(), pos.getY(), pos.getZ()));
        }
        return worldView.isUnobstructed(state, pos, context);
    }
}
