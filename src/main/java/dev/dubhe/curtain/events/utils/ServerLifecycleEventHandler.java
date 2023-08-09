package dev.dubhe.curtain.events.utils;

import dev.dubhe.curtain.Curtain;
import dev.dubhe.curtain.api.rules.RuleManager;
import dev.dubhe.curtain.commands.LogCommand;
import dev.dubhe.curtain.commands.PlayerCommand;
import dev.dubhe.curtain.commands.RuleCommand;
import dev.dubhe.curtain.events.WorldTickEvent;
import dev.dubhe.curtain.utils.PlanExecution;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;

public class ServerLifecycleEventHandler {
    @SubscribeEvent
    public void onServerAboutToStart(FMLServerAboutToStartEvent event) {
        Curtain.rules = new RuleManager(event.getServer(), Curtain.MODID);
        Curtain.minecraftServer = event.getServer();
        Curtain.planExecution = new PlanExecution();
        RuleCommand.register(event.getServer().getCommands().getDispatcher(), Curtain.rules);
        PlayerCommand.register(event.getServer().getCommands().getDispatcher());
        LogCommand.register(event.getServer().getCommands().getDispatcher());
    }

    @SubscribeEvent
    public void onServerTick(WorldTickEvent event) {
        if (event.getWorld() instanceof ServerWorld) {
            ServerWorld level = (ServerWorld) event.getWorld();
            long time = level.getGameTime();
            if (Curtain.planExecution != null) {
                Curtain.planExecution.execute(time);
            }
        }
    }

    @SubscribeEvent
    public void onServerStopped(FMLServerStoppedEvent event) {
        Curtain.rules.saveToFile();
    }
}
