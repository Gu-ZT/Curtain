package dev.dubhe.curtain.events.rules.fake_player_auto_fish;

import dev.dubhe.curtain.Curtain;
import dev.dubhe.curtain.CurtainRules;
import dev.dubhe.curtain.utils.PlanExecution;
import dev.dubhe.curtain.events.events.FishingHookEvent;
import dev.dubhe.curtain.features.player.fakes.IServerPlayer;
import dev.dubhe.curtain.features.player.helpers.EntityPlayerActionPack;
import dev.dubhe.curtain.features.player.helpers.EntityPlayerActionPack.Action;
import dev.dubhe.curtain.features.player.helpers.EntityPlayerActionPack.ActionType;
import dev.dubhe.curtain.features.player.patches.EntityPlayerMPFake;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nonnull;

public class FishingHookEventHandler {
    @SubscribeEvent
    public void autoFish(@Nonnull FishingHookEvent.Catching event) {
        PlayerEntity player = event.getOwner();
        if (CurtainRules.fakePlayerAutoFish && player instanceof EntityPlayerMPFake) {
            EntityPlayerMPFake fake = (EntityPlayerMPFake) player;
            EntityPlayerActionPack ap = ((IServerPlayer) fake).getActionPack();
            long time = player.level.getGameTime();
            PlanExecution plans = Curtain.planExecution;
            if (null != plans) {
                plans.post(time + 5, time1 -> ap.start(ActionType.USE, Action.once()));
                plans.post(time + 15, time1 -> ap.start(ActionType.USE, Action.once()));
            }
        }
    }
}
