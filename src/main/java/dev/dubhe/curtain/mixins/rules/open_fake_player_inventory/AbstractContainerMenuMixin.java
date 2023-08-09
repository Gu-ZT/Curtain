package dev.dubhe.curtain.mixins.rules.open_fake_player_inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Container.class)
public abstract class AbstractContainerMenuMixin {


    @Shadow
    @Final
    public List<Slot> slots;

    @Inject(method = "doClick", at = @At("HEAD"), cancellable = true)
    private void onClick(int mouseX, int mouseY, ClickType clickType, PlayerEntity player, CallbackInfoReturnable<ItemStack> cir) {
        if (mouseX < 0) return;
        Slot slot = slots.get(mouseX);
        ItemStack itemStack = slot.getItem();
        if (itemStack.getTag() != null) {
            if (itemStack.getTag().get("CurtainGUIItem") != null) {
                if (itemStack.getTag().getBoolean("CurtainGUIItem")) {
                    itemStack.setCount(0);
                    cir.setReturnValue(itemStack);
                    cir.cancel();
                }
            }
        }
    }
}
