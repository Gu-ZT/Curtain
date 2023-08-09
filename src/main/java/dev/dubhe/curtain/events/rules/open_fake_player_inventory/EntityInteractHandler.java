package dev.dubhe.curtain.events.rules.open_fake_player_inventory;

import dev.dubhe.curtain.CurtainRules;
import dev.dubhe.curtain.features.player.patches.EntityPlayerMPFake;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static dev.dubhe.curtain.features.player.menu.MenuHashMap.FAKE_PLAYER_INVENTORY_MENU_MAP;

public class EntityInteractHandler {
    @SubscribeEvent
    public void onInteractWithFakePlayer(PlayerInteractEvent.EntityInteract entityInteract) {
        if (entityInteract.getTarget() instanceof EntityPlayerMPFake fakeplayer) {
            SimpleNamedContainerProvider provider = null;
            if (CurtainRules.openFakePlayerEnderChest && entityInteract.getEntity().isShiftKeyDown()) {
                provider = new SimpleNamedContainerProvider(
                        (i, inventory, p) ->
                                ChestContainer.threeRows(
                                        i,
                                        inventory,
                                        fakeplayer.getEnderChestInventory()
                                ),
                        fakeplayer.getDisplayName()
                );
            } else if (CurtainRules.openFakePlayerInventory) {
                provider = new SimpleNamedContainerProvider(
                        (i, inventory, p) ->
                                ChestContainer.sixRows(
                                        i,
                                        inventory,
                                        FAKE_PLAYER_INVENTORY_MENU_MAP.get(fakeplayer)
                                ),
                        fakeplayer.getDisplayName()
                );
            }
            if (provider != null)
                entityInteract.getPlayer().openMenu(provider);
        }

    }
}
