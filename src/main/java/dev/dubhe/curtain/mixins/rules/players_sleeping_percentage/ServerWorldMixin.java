package dev.dubhe.curtain.mixins.rules.players_sleeping_percentage;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {
    @Shadow
    @Final
    private List<ServerPlayerEntity> players;

    @Shadow
    private boolean allPlayersSleeping;

    @Inject(method = "updateSleepingPlayerList", cancellable = true, at = @At("HEAD"))
    private void updateOnePlayerSleeping(CallbackInfo ci) {
        if (CurtainRules.playersSleepingPercentage != 100) {
            allPlayersSleeping = false;
            int playerNumber = 0;
            int sleepingPlayerNumber = 0;
            for (ServerPlayerEntity p : players) {
                if (!p.isSpectator()) {
                    playerNumber++;
                    if (p.isSleeping()) {
                        sleepingPlayerNumber++;
                        ci.cancel();
                        return;
                    }
                }
            }
            allPlayersSleeping = sleepingPlayerNumber > (playerNumber * (CurtainRules.playersSleepingPercentage / 100));
            ci.cancel();
        }
    }

    @Redirect(method = "tick", at = @At(
            value = "INVOKE",
            target = "Ljava/util/stream/Stream;noneMatch(Ljava/util/function/Predicate;)Z"
    ))
    private <T extends ServerPlayerEntity> boolean noneMatchSleep(Stream<T> instance, Predicate<? super T> predicate) {
        if (CurtainRules.playersSleepingPercentage != 100)
            return instance.anyMatch((p) -> !p.isSpectator() && p.isSleepingLongEnough());
        return instance.noneMatch(predicate);
    }
}
