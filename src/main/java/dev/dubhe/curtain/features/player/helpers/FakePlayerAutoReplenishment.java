package dev.dubhe.curtain.features.player.helpers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class FakePlayerAutoReplenishment {

    public static void autoReplenishment(PlayerEntity fakePlayer) {
        NonNullList<ItemStack> itemStackList = fakePlayer.inventory.items;
        replenishment(fakePlayer.getMainHandItem(), itemStackList);
        replenishment(fakePlayer.getOffhandItem(), itemStackList);
    }

    public static void replenishment(ItemStack itemStack, NonNullList<ItemStack> itemStackList) {
        int count = itemStack.getMaxStackSize() / 2;
        if (itemStack.getCount() <= 8 && count > 8) {
            for (ItemStack itemStack1 : itemStackList) {
                if (itemStack1 == ItemStack.EMPTY || itemStack1 == itemStack) continue;
                if (ItemStack.isSame(itemStack1, itemStack)) {
                    if (itemStack1.getCount() > count) {
                        itemStack.setCount(itemStack.getCount() + count);
                        itemStack1.setCount(itemStack1.getCount() - count);
                    } else {
                        itemStack.setCount(itemStack.getCount() + itemStack1.getCount());
                        itemStack1.setCount(0);
                    }
                    break;
                }
            }
        }
    }
}
