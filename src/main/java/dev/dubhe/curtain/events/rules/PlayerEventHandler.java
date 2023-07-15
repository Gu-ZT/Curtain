package dev.dubhe.curtain.events.rules;

import dev.dubhe.curtain.CurtainRules;
import dev.dubhe.curtain.events.events.ItemStackEvent;
import dev.dubhe.curtain.features.player.helpers.FakePlayerAutoReplaceTool;
import dev.dubhe.curtain.features.player.helpers.FakePlayerAutoReplenishment;
import dev.dubhe.curtain.features.player.menu.FakePlayerInventoryMenu;
import dev.dubhe.curtain.features.player.patches.EntityPlayerMPFake;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static dev.dubhe.curtain.features.player.menu.MenuHashMap.FAKE_PLAYER_INVENTORY_MENU_MAP;

public class PlayerEventHandler {
    // 假人背包(openFakePlayerInventory)
    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent playerTickEvent) {
        if (CurtainRules.openFakePlayerInventory &&
                playerTickEvent.player instanceof ServerPlayer serverPlayer &&
                serverPlayer instanceof EntityPlayerMPFake &&
                serverPlayer.isAlive()
        ) {
            FAKE_PLAYER_INVENTORY_MENU_MAP.get(serverPlayer).tick();
        }
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        FAKE_PLAYER_INVENTORY_MENU_MAP.put(event.getEntity(), new FakePlayerInventoryMenu(event.getEntity()));
    }

    @SubscribeEvent
    public void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        FAKE_PLAYER_INVENTORY_MENU_MAP.remove(event.getEntity());
    }

    // 工具缺失修复(missingTools)
    @SubscribeEvent
    public void onBreak(ItemStackEvent.BreakSpeed event) {
        BlockState state = event.getState();
        event.setSpeed(
                CurtainRules.missingTools && state.getBlock().getSoundType(state) == SoundType.GLASS ? event.getSpeed() : event.getOriginalSpeed()
        );
    }

    // 假人补货(fakePlayerAutoReplenishment)
    @SubscribeEvent
    public void onUse(ItemStackEvent.Use event) {
        if (CurtainRules.fakePlayerAutoReplenishment && event.getPlayer() instanceof EntityPlayerMPFake fakePlayer) {
            FakePlayerAutoReplenishment.autoReplenishment(fakePlayer);
        }
    }

    // 假人补货(fakePlayerAutoReplenishment)
    @SubscribeEvent
    public void onHurtAndBreak(ItemStackEvent.HurtAndBreak event) {
        if (CurtainRules.fakePlayerAutoReplaceTool && event.getPlayer() instanceof EntityPlayerMPFake fakePlayer) {
            FakePlayerAutoReplaceTool.autoReplaceTool(fakePlayer);
        }
    }
}
