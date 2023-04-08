package dev.dubhe.curtain.events.utils;

import dev.dubhe.curtain.CurtainRules;
import dev.dubhe.curtain.features.logging.LoggerManager;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ServerEventHandler {
    private int timer = 0;

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (timer <= 0) {
            timer = CurtainRules.HUDLoggerUpdateInterval;
            LoggerManager.updateHUD();
        }
        timer -= 1;
    }

    @SubscribeEvent
    public void onPlayLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayerEntity player) {
            String playerName = player.getName().getString();
            if (CurtainRules.defaultLoggers.contentEquals("none")) {
                return;
            }
            if (!LoggerManager.hasSubscribedLogger(playerName)) {
                String[] logs = CurtainRules.defaultLoggers.replace(" ", "").split(",");
                LoggerManager.subscribeLogger(playerName, logs);
            }
        }
    }

    @SubscribeEvent
    public void onPlayLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayerEntity player) {
            String playerName = player.getName().getString();
            LoggerManager.unsubscribeAllLogger(playerName);
        }
    }
}
