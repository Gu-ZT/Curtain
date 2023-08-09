package dev.dubhe.curtain.mixins.rules.better_wood_strip;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(AxeItem.class)
public abstract class AxeItemMixin {
    @Inject(method = "useOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getToolModifiedState(Lnet/minecraft/world/item/context/UseOnContext;Lnet/minecraftforge/common/ToolAction;Z)Lnet/minecraft/world/level/block/state/BlockState;", ordinal = 0), cancellable = true)
    private void stripped(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack itemStack = context.getItemInHand();
        String name = itemStack.getHoverName().getString();
        if (!name.contains("Strip") && !name.contains("去皮") && CurtainRules.betterWoodStrip) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }
}
