package dev.dubhe.curtain.events.utils;

import dev.dubhe.curtain.CurtainRules;
import dev.dubhe.curtain.features.logging.LoggerManager;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerEventHandler {
    private int timer = 0;

    @SubscribeEvent
    public void onPlayerLifeTick(PlayerEvent.LivingTickEvent event) {
        if (timer <= 0) {
            timer = CurtainRules.HUDLoggerUpdateInterval;
            LoggerManager.updateHUD();
        }
        timer -= 1;
    }
}
