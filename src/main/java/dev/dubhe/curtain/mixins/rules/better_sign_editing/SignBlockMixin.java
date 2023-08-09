package dev.dubhe.curtain.mixins.rules.better_sign_editing;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.block.AbstractSignBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractSignBlock.class)
public abstract class SignBlockMixin {
    private final AbstractSignBlock self = (AbstractSignBlock) (Object) this;

    @Inject(method = "use", at = @At(value = "HEAD"), cancellable = true)
    public void use(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit, CallbackInfoReturnable<ActionResultType> cir) {
        ItemStack itemStack = player.getItemInHand(hand);
        String name = itemStack.getHoverName().getString();
        if (CurtainRules.betterSignEditing && itemStack.getItem() == (Items.FEATHER) && (name.contains("pen") || name.contains("笔"))) {
            SignTileEntity sign = (SignTileEntity) level.getBlockEntity(pos);
            if (!level.isClientSide && sign != null) {
                player.openTextEdit(sign);
            }
            cir.setReturnValue(ActionResultType.SUCCESS);
        }
    }
}
