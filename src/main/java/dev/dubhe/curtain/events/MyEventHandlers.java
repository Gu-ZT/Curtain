package dev.dubhe.curtain.events;

import dev.dubhe.curtain.events.rules.PlayerEventHandler;
import dev.dubhe.curtain.events.rules.fake_player_auto_fish.FishingHookEventHandler;
import dev.dubhe.curtain.events.rules.fake_player_auto_replenishment.LivingEntityEventHandler;
import dev.dubhe.curtain.events.rules.open_fake_player_inventory.EntityInteractHandler;
import dev.dubhe.curtain.events.utils.ServerEventHandler;
import dev.dubhe.curtain.events.utils.ServerLifecycleEventHandler;
import dev.dubhe.curtain.events.utils.WorldEventHandler;
import net.minecraftforge.common.MinecraftForge;

public class MyEventHandlers {
    public static void register() {
        MinecraftForge.EVENT_BUS.register(new ServerLifecycleEventHandler());
        MinecraftForge.EVENT_BUS.register(new WorldEventHandler());
        MinecraftForge.EVENT_BUS.register(new ServerEventHandler());
        // openFakePlayerInventory
        MinecraftForge.EVENT_BUS.register(new EntityInteractHandler());

        MinecraftForge.EVENT_BUS.register(new FishingHookEventHandler());

        MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());
        // fakePlayerAutoFish
        MinecraftForge.EVENT_BUS.register(new FishingHookEventHandler());
        // fakePlayerAutoReplenishment
        MinecraftForge.EVENT_BUS.register(new LivingEntityEventHandler());
    }
}
