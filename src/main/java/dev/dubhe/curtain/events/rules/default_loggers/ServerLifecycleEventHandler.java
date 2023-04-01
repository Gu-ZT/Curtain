package dev.dubhe.curtain.events.rules.default_loggers;

import dev.dubhe.curtain.Curtain;
import dev.dubhe.curtain.features.logging.LoggerManager;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

public class ServerLifecycleEventHandler {
    @SubscribeEvent
    public void onServerAboutToStart(@NotNull ServerAboutToStartEvent event) {
        if (null != Curtain.loggers) Curtain.loggers.destroy();
        Curtain.loggers = new LoggerManager();
    }
}
