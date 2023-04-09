package dev.dubhe.curtain.mixins.rules.open_fake_player_inventory;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerMenu.class)
public abstract class AbstractContainerMenuMixin {
    @Final
    @Shadow
    public NonNullList<Slot> slots;

    @Inject(method = "doClick", at = @At("HEAD"), cancellable = true)
    private void onClick(int mouseX, int mouseY, ClickType clickType, Player player, CallbackInfo ci) {
        if (mouseX < 0) return;
        Slot slot = slots.get(mouseX);
        ItemStack itemStack = slot.getItem();
        if (itemStack.getTag() != null) {
            if (itemStack.getTag().get("CurtainGUIItem") != null) {
                if (itemStack.getTag().getBoolean("CurtainGUIItem")) {
                    itemStack.setCount(0);
                    ci.cancel();
                }
            }
        }
    }
}
