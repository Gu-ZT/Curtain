package dev.dubhe.curtain.events;

import dev.dubhe.curtain.events.utils.LevelEventHandler;
import dev.dubhe.curtain.events.utils.ServerLifecycleEventHandler;
import net.minecraftforge.common.MinecraftForge;

public class MyEventHandlers {
    public static void register() {
        MinecraftForge.EVENT_BUS.register(new ServerLifecycleEventHandler());
        MinecraftForge.EVENT_BUS.register(new LevelEventHandler());
        MinecraftForge.EVENT_BUS.register(new dev.dubhe.curtain.events.rules.default_loggers.PlayerEventHandler());
        MinecraftForge.EVENT_BUS.register(new dev.dubhe.curtain.events.rules.default_loggers.ServerLifecycleEventHandler());
    }
}
