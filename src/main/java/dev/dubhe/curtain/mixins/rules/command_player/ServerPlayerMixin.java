package dev.dubhe.curtain.mixins.rules.command_player;

import com.mojang.authlib.GameProfile;
import dev.dubhe.curtain.features.player.fakes.IServerPlayer;
import dev.dubhe.curtain.features.player.helpers.EntityPlayerActionPack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
<<<<<<< HEAD:src/main/java/dev/dubhe/curtain/mixins/ServerPlayerMixin.java
=======
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
>>>>>>> a627c718d1289379cb1bdfcbd950f41dca3e016b:src/main/java/dev/dubhe/curtain/mixins/rules/command_player/ServerPlayerMixin.java
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin implements IServerPlayer {
    @Shadow
    @Final
    private static Logger LOGGER;
    public EntityPlayerActionPack actionPack;

    @Override
    public EntityPlayerActionPack getActionPack() {
        return actionPack;
    }

    @Inject(method = "<init>", at = @At(value = "RETURN"))
<<<<<<< HEAD:src/main/java/dev/dubhe/curtain/mixins/ServerPlayerMixin.java
    private void onServerPlayerEntityContructor(MinecraftServer p_254143_, ServerLevel p_254435_, GameProfile p_253651_, CallbackInfo ci) {
=======
    private void onServerPlayerEntityContructor(
            MinecraftServer minecraftServer_1,
            ServerLevel serverWorld_1,
            GameProfile gameProfile_1,
            CallbackInfo ci) {
>>>>>>> a627c718d1289379cb1bdfcbd950f41dca3e016b:src/main/java/dev/dubhe/curtain/mixins/rules/command_player/ServerPlayerMixin.java
        this.actionPack = new EntityPlayerActionPack((ServerPlayer) (Object) this);
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void onTick(CallbackInfo ci) {
        try {
            actionPack.onUpdate();
        } catch (StackOverflowError soe) {
            LOGGER.warn("Caused stack overflow when performing player action", soe);
        } catch (Throwable exc) {
            LOGGER.warn("Error executing player tasks ", exc);
        }
    }
}
