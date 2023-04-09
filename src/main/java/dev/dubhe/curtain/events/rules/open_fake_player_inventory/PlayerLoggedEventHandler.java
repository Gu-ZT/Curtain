package dev.dubhe.curtain.events.rules.open_fake_player_inventory;

import dev.dubhe.curtain.features.player.menu.FakePlayerInventoryMenu;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static dev.dubhe.curtain.features.player.menu.MenuHashMap.FAKE_PLAYER_INVENTORY_MENU_MAP;

public class PlayerLoggedEventHandler {
    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        FAKE_PLAYER_INVENTORY_MENU_MAP.put(event.getEntity(), new FakePlayerInventoryMenu(event.getEntity()));
    }

    @SubscribeEvent
    public void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        FAKE_PLAYER_INVENTORY_MENU_MAP.remove(event.getEntity());
    }
}
