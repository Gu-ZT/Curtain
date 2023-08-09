package dev.dubhe.curtain.mixins.rules.empty_shulker_box_stack_always;

import dev.dubhe.curtain.CurtainRules;
import dev.dubhe.curtain.utils.InventoryUtils;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow
    public abstract Item getItem();

    @Inject(method = "getMaxStackSize", at = @At("HEAD"), cancellable = true)
    private void allowEmptyShulkerBoxStacking(CallbackInfoReturnable<Integer> cir)
    {
        if (CurtainRules.emptyShulkerBoxStackAlways && this.getItem() instanceof BlockItem item)
        {
            if (item.getBlock() instanceof ShulkerBoxBlock && !InventoryUtils.shulkerBoxHasItems((ItemStack) (Object) this))
            {
                cir.setReturnValue(CurtainRules.shulkerBoxStackSize);
            }
        }
    }
}
