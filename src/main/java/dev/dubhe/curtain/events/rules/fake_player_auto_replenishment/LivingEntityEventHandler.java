package dev.dubhe.curtain.events.rules.fake_player_auto_replenishment;

import dev.dubhe.curtain.CurtainRules;
import dev.dubhe.curtain.features.player.patches.EntityPlayerMPFake;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LivingEntityEventHandler {
    @SubscribeEvent
    public void onPlayerUsing(LivingEntityUseItemEvent.Finish event) {
        System.out.println(event.getEntity() instanceof EntityPlayerMPFake);
        if (CurtainRules.fakePlayerAutoReplenishment && event.getEntity() instanceof EntityPlayerMPFake fakePlayer) {
            NonNullList<ItemStack> itemStackList = fakePlayer.getInventory().items;
            replenishment(fakePlayer.getMainHandItem(), itemStackList);
            replenishment(fakePlayer.getOffhandItem(), itemStackList);
        }
    }

    public static void replenishment(ItemStack itemStack, NonNullList<ItemStack> itemStackList) {
        int count = itemStack.getMaxStackSize() / 2;
        if (itemStack.getCount() <= 8 && count > 8) {
            for (ItemStack itemStack1 : itemStackList) {
                if (itemStack1 == ItemStack.EMPTY || itemStack1 == itemStack) continue;
                if (ItemStack.isSameItemSameTags(itemStack1, itemStack)) {
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
