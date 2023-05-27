package dev.dubhe.curtain.events.rules.open_fake_player_inventory;

import dev.dubhe.curtain.CurtainRules;
import dev.dubhe.curtain.features.player.patches.EntityPlayerMPFake;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static dev.dubhe.curtain.features.player.menu.MenuHashMap.FAKE_PLAYER_INVENTORY_MENU_MAP;

public class EntityInteractHandler {
    @SubscribeEvent
    public void onInteractWithFakePlayer(PlayerInteractEvent.EntityInteract entityInteract) {
        if (entityInteract.getTarget() instanceof EntityPlayerMPFake fakeplayer) {
            SimpleMenuProvider provider = null;
            if (CurtainRules.openFakePlayerEnderChest && entityInteract.getEntity().isShiftKeyDown()) {
                provider = new SimpleMenuProvider(
                        (i, inventory, p) ->
                                ChestMenu.threeRows(
                                        i,
                                        inventory,
                                        fakeplayer.getEnderChestInventory()
                                ),
                        fakeplayer.getDisplayName()
                );
            } else if (CurtainRules.openFakePlayerInventory) {
                provider = new SimpleMenuProvider(
                        (i, inventory, p) ->
                                ChestMenu.sixRows(
                                        i,
                                        inventory,
                                        FAKE_PLAYER_INVENTORY_MENU_MAP.get(fakeplayer)
                                ),
                        fakeplayer.getDisplayName()
                );
            }
            if (provider != null)
                entityInteract.getEntity().openMenu(provider);
        }

    }
}
