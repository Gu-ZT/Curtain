package dev.dubhe.curtain.events.rules.default_loggers;

import dev.dubhe.curtain.Curtain;
import dev.dubhe.curtain.CurtainRules;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerEventHandler {
    @SubscribeEvent
    public void onPlayerTabListNameFormat(PlayerEvent.LivingTickEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            if (!"none".equals(CurtainRules.defaultLoggers)) player.setTabListFooter(Curtain.loggers.display(player));
            else player.setTabListFooter(Component.empty());
        }
    }
}
