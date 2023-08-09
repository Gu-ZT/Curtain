package dev.dubhe.curtain.mixins.rules.better_wood_strip;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(AxeItem.class)
public abstract class AxeItemMixin {
    @Inject(method = "useOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getToolModifiedState(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraftforge/common/ToolType;)Lnet/minecraft/block/BlockState;", ordinal = 0), cancellable = true)
    private void stripped(ItemUseContext pContext, CallbackInfoReturnable<ActionResultType> cir) {
        ItemStack itemStack = pContext.getItemInHand();
        String name = itemStack.getHoverName().getString();
        if (!name.contains("Strip") && !name.contains("去皮") && CurtainRules.betterWoodStrip) {
            cir.setReturnValue(ActionResultType.PASS);
        }
    }
}
