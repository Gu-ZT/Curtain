package dev.dubhe.curtain.event;

import dev.dubhe.curtain.Curtain;
import dev.dubhe.curtain.api.rules.RuleManager;
import dev.dubhe.curtain.commands.RuleCommand;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

public class ServerLifecycleEventHandler {
    @SubscribeEvent
    public void onServerAboutToStart(@NotNull ServerAboutToStartEvent event) {
        Curtain.manager = new RuleManager(event.getServer(), Curtain.MODID);
        RuleCommand.register(event.getServer().getCommands().getDispatcher(), Curtain.manager);
    }

    @SubscribeEvent
    public void onServerStopped(@NotNull ServerStoppedEvent event) {
        Curtain.manager.saveToFile();
    }
}
