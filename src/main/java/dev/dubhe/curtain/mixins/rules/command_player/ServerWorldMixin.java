package dev.dubhe.curtain.mixins.rules.command_player;

import dev.dubhe.curtain.features.player.patches.EntityPlayerMPFake;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.concurrent.TickDelayedTask;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {
    @Shadow
    public abstract MinecraftServer getServer();

    @Shadow
    boolean tickingEntities;

    @Shadow
    @Deprecated
    public abstract void onEntityRemoved(Entity p_217484_1_);

    @Shadow
    public abstract void updateSleepingPlayerList();

    @SuppressWarnings("UnnecessaryReturnStatement")
    @Inject(method = "removePlayerImmediately", at = @At("HEAD"))
    private void removePlayer(ServerPlayerEntity player, CallbackInfo ci) {
        player.remove(false);
        if (!(tickingEntities && player instanceof EntityPlayerMPFake)) this.onEntityRemoved(player);
        else {
            this.getServer().tell(new TickDelayedTask(getServer().getTickCount(), () ->
            {
                this.onEntityRemoved(player);
                player.hasChangedDimension();
            }));
        }
        this.updateSleepingPlayerList();
        return;
    }
}
