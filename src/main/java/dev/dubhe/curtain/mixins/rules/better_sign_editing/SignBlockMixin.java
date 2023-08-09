package dev.dubhe.curtain.mixins.rules.better_sign_editing;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SignBlock.class)
public abstract class SignBlockMixin {
    private final SignBlock self = (SignBlock) (Object) this;

    @Inject(method = "use", at = @At(value = "HEAD"), cancellable = true)
    public void use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack itemStack = player.getItemInHand(hand);
        String name = itemStack.getHoverName().getString();
        if (CurtainRules.betterSignEditing && itemStack.is(Items.FEATHER) && (name.contains("pen") || name.contains("笔"))) {
            SignBlockEntity sign = (SignBlockEntity) level.getBlockEntity(pos);
            if (!level.isClientSide && sign != null) {
                player.openTextEdit(sign);
            }
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}
