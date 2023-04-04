package dev.dubhe.curtain.events;

import dev.dubhe.curtain.events.rules.open_fake_player_inventory.PlayerLoggedEventHandler;
import dev.dubhe.curtain.events.rules.open_fake_player_inventory.PlayerTickEventHandler;
import dev.dubhe.curtain.events.rules.open_fake_player_inventory.entityInteractHandler;
import dev.dubhe.curtain.events.utils.LevelEventHandler;
import dev.dubhe.curtain.events.utils.ServerEventHandler;
import dev.dubhe.curtain.events.utils.ServerLifecycleEventHandler;
import net.minecraftforge.common.MinecraftForge;

public class MyEventHandlers {
    public static void register() {
        MinecraftForge.EVENT_BUS.register(new ServerLifecycleEventHandler());
        MinecraftForge.EVENT_BUS.register(new LevelEventHandler());
        MinecraftForge.EVENT_BUS.register(new ServerEventHandler());

        MinecraftForge.EVENT_BUS.register(new entityInteractHandler());
        MinecraftForge.EVENT_BUS.register(new PlayerLoggedEventHandler());
        MinecraftForge.EVENT_BUS.register(new PlayerTickEventHandler());
    }
}
