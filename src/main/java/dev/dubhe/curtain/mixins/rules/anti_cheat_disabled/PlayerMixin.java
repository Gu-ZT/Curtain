package dev.dubhe.curtain.mixins.rules.anti_cheat_disabled;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerMixin {
    @Shadow
    public abstract ItemStack getItemBySlot(EquipmentSlotType equipment);

    @Shadow
    public abstract void startFallFlying();

    @Inject(method = "tryToStartFallFlying", at = @At("HEAD"), cancellable = true)
    private void allowDeploys(CallbackInfoReturnable<Boolean> cir) {
        if (CurtainRules.antiCheatDisabled && (PlayerEntity) (Object) this instanceof ServerPlayerEntity && ((ServerPlayerEntity) (Object) this).getServer() != null && ((ServerPlayerEntity) (Object) this).getServer().isDedicatedServer()) {
            ItemStack itemStack_1 = getItemBySlot(EquipmentSlotType.CHEST);
            if (itemStack_1.getItem() == Items.ELYTRA && ElytraItem.isFlyEnabled(itemStack_1)) {
                startFallFlying();
                cir.setReturnValue(true);
            }
        }
    }
}
