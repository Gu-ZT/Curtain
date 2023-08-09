package dev.dubhe.curtain.mixins.rules.better_fence_gate_placement;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.block.DirectionalBlock.FACING;

@Mixin(FenceGateBlock.class)
public abstract class FenceGateBlockMixin {
    @Shadow
    @Final
    public static BooleanProperty OPEN;
    @Shadow
    @Final
    public static BooleanProperty POWERED;
    @Shadow
    @Final
    public static BooleanProperty IN_WALL;
    FenceGateBlock self = (FenceGateBlock) (Object) this;

    @Inject(method = "getStateForPlacement", at = @At(value = "RETURN"), cancellable = true)
    private void getStateForPlacement(BlockItemUseContext pContext, CallbackInfoReturnable<BlockState> cir) {
        World level = pContext.getLevel();
        BlockPos blockPos = pContext.getClickedPos();
        BlockState blockState = level.getBlockState(blockPos);
        if (CurtainRules.betterFenceGatePlacement && level.getBlockState(blockPos).getBlock() instanceof FenceGateBlock) {
            boolean bl = level.hasNeighborSignal(blockPos) || blockState.getValue(OPEN);
            boolean bl1 = level.hasNeighborSignal(blockPos);
            Direction direction = blockState.getValue(FACING);
            Direction.Axis axis = direction.getAxis();
            boolean bl2 = axis == Direction.Axis.Z && (this.isWall(level.getBlockState(blockPos.west())) || this.isWall(level.getBlockState(blockPos.east()))) || axis == Direction.Axis.X && (this.isWall(level.getBlockState(blockPos.north())) || this.isWall(level.getBlockState(blockPos.south())));
            cir.setReturnValue(self.defaultBlockState().setValue(FACING, direction).setValue(OPEN, bl).setValue(POWERED, bl1).setValue(IN_WALL, bl2));
        }
    }

    @Shadow
    protected abstract boolean isWall(BlockState state);
}
