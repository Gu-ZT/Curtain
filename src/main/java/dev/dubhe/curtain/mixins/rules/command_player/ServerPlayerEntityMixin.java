package dev.dubhe.curtain.mixins.rules.command_player;

import com.mojang.authlib.GameProfile;
import dev.dubhe.curtain.features.player.fakes.IServerPlayer;
import dev.dubhe.curtain.features.player.helpers.EntityPlayerActionPack;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.world.server.ServerWorld;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin implements IServerPlayer {
    @Final
    @Shadow
    private static Logger LOGGER;
    public EntityPlayerActionPack actionPack;

    public EntityPlayerActionPack getActionPack() {
        return actionPack;
    }

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void onServerPlayerEntityContructor(
            MinecraftServer minecraftServer_1,
            ServerWorld serverWorld_1,
            GameProfile gameProfile_1,
            PlayerInteractionManager serverPlayerInteractionManager_1,
            CallbackInfo ci) {
        this.actionPack = new EntityPlayerActionPack((ServerPlayerEntity) (Object) this);
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void onTick(CallbackInfo ci) {
        try {
            actionPack.onUpdate();
        } catch (StackOverflowError soe) {
            LOGGER.fatal("Caused stack overflow when performing player action");
        } catch (Throwable exc) {
            LOGGER.fatal("Error executing player tasks " + exc.getMessage());
            exc.printStackTrace();
        }
    }
}
