package dev.dubhe.curtain.features.player.helpers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

public class FakePlayerAutoReplaceTool {
    public static void autoReplaceTool(PlayerEntity fakePlayer) {
        ItemStack mainHand = fakePlayer.getMainHandItem();
        ItemStack offHand = fakePlayer.getOffhandItem();
        if (!mainHand.isEmpty() && (mainHand.getMaxDamage() - mainHand.getDamageValue()) <= 10)
            replaceTool(EquipmentSlotType.MAINHAND, fakePlayer);
        if (!offHand.isEmpty() && (offHand.getMaxDamage() - offHand.getDamageValue()) <= 10)
            replaceTool(EquipmentSlotType.OFFHAND, fakePlayer);
    }

    public static void replaceTool(EquipmentSlotType slot, PlayerEntity fakePlayer) {
        ItemStack itemStack = fakePlayer.getItemBySlot(slot);
        for (int i = 0; i < 36; i++) {
            ItemStack itemStack1 = fakePlayer.inventory.getItem(i);
            if (itemStack1 == ItemStack.EMPTY || itemStack1 == itemStack) continue;
            if (itemStack1.getItem().getClass() == itemStack.getItem().getClass() && (itemStack1.getMaxDamage() - itemStack1.getDamageValue()) > 10) {
                ItemStack itemStack2 = itemStack1.copy();
                fakePlayer.inventory.setItem(i, itemStack);
                fakePlayer.setItemSlot(slot, itemStack2);
                break;
            }
        }
    }
}
