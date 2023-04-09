package dev.dubhe.curtain.mixins.rules.cactus;

import dev.dubhe.curtain.utils.BlockRotator;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DispenserBlock.class)
public abstract class DispenserBlockMixin {
    @Inject(method = "getDispenseMethod", at = @At("HEAD"), cancellable = true)
    private void registerCurtainBehaviors(ItemStack stack, CallbackInfoReturnable<IDispenseItemBehavior> cir) {
        Item item = stack.getItem();
        if (item == Items.CACTUS) {
            cir.setReturnValue(new BlockRotator.CactusDispenserBehaviour());
        }
    }
}
