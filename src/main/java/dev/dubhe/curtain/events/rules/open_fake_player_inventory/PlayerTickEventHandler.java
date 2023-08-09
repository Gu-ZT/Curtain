package dev.dubhe.curtain.events.rules.open_fake_player_inventory;

import dev.dubhe.curtain.CurtainRules;
import dev.dubhe.curtain.features.player.patches.EntityPlayerMPFake;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static dev.dubhe.curtain.features.player.menu.MenuHashMap.FAKE_PLAYER_INVENTORY_MENU_MAP;

public class PlayerTickEventHandler {
    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent playerTickEvent) {
        if (CurtainRules.openFakePlayerInventory &&
                playerTickEvent.player instanceof EntityPlayerMPFake &&
                playerTickEvent.player.isAlive()
        ) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) playerTickEvent.player;
            FAKE_PLAYER_INVENTORY_MENU_MAP.get(serverPlayer).tick();
        }
    }
}
