package dev.dubhe.curtain.mixins.rules.ctrl_q_crafting_fix;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
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

    @Shadow
    public abstract ItemStack clicked(int p_184996_1_, int p_184996_2_, ClickType p_184996_3_, PlayerEntity p_184996_4_);

    @Shadow
    public abstract void broadcastChanges();


    @Inject(method = "doClick", at = @At(value = "HEAD"), cancellable = true)
    private void onThrowClick(int slotId, int clickData, ClickType clickType, PlayerEntity player, CallbackInfoReturnable<ItemStack> cir) {
        if (clickType == ClickType.THROW && CurtainRules.ctrlQCraftingFix && player.inventory.getCarried().isEmpty() && slotId >= 0) {
            ItemStack itemStack_1 = ItemStack.EMPTY;
            Slot slot_4 = slots.get(slotId);
            if (slot_4 != null && slot_4.hasItem() && slot_4.mayPickup(player)) {
                if (slotId == 0 && clickData == 1) {
                    Item craftedItem = slot_4.getItem().getItem();
                    while (slot_4.hasItem() && slot_4.getItem().getItem() == craftedItem) {
                        this.clicked(slotId, 0, ClickType.THROW, player);
                    }
                    this.broadcastChanges();
                    cir.setReturnValue(itemStack_1);
                    cir.cancel();
                }
            }
        }
    }
}
