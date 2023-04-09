package dev.dubhe.curtain.mixins.rules.ctrl_q_crafting_fix;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerMenu.class)
public abstract class AbstractContainerMenuMixin {
    @Shadow
    public abstract ItemStack getCarried();

    @Shadow
    @Final
    public NonNullList<Slot> slots;

    @Shadow
    public abstract void clicked(int p_150400_, int p_150401_, ClickType p_150402_, Player p_150403_);

    @Shadow
    public abstract void broadcastChanges();

    @Shadow
    protected abstract void resetQuickCraft();

    @Inject(method = "doClick", at = @At(value = "HEAD"), cancellable = true)
    private void onThrowClick(int slotId, int clickData, ClickType clickType, Player player, CallbackInfo ci) {
        if (clickType == ClickType.THROW && CurtainRules.ctrlQCraftingFix && this.getCarried().isEmpty() && slotId >= 0) {
            Slot slot = slots.get(slotId);
            if (slot.hasItem() && slot.mayPickup(player)) {
                if (slotId == 0 && clickData == 1) {
                    Item craftedItem = slot.getItem().getItem();
                    while (slot.hasItem() && slot.getItem().getItem() == craftedItem) {
                        this.clicked(slotId, 0, ClickType.THROW, player);
                    }
                    this.broadcastChanges();
                    this.resetQuickCraft();
                    ci.cancel();
                }
            }
        }
    }
}
