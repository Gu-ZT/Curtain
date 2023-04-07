package dev.dubhe.curtain.mixins.rules.command_player;

import com.mojang.authlib.GameProfile;
import dev.dubhe.curtain.features.player.patches.EntityPlayerMPFake;
import dev.dubhe.curtain.features.player.patches.NetHandlerPlayServerFake;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Mixin(PlayerList.class)
public class PlayerListMixin {
    @Shadow @Final private MinecraftServer server;

    @Inject(method = "load", at = @At(value = "RETURN", shift = At.Shift.BEFORE))
    private void fixStartingPos(ServerPlayer serverPlayerEntity_1, CallbackInfoReturnable<CompoundTag> cir)
    {
        if (serverPlayerEntity_1 instanceof EntityPlayerMPFake)
        {
            ((EntityPlayerMPFake) serverPlayerEntity_1).fixStartingPosition.run();
        }
    }

    @Redirect(method = "placeNewPlayer", at = @At(value = "NEW", target = "net/minecraft/server/network/ServerGamePacketListenerImpl"))
    private ServerGamePacketListenerImpl replaceNetworkHandler(MinecraftServer server, Connection networkManager, ServerPlayer playerIn)
    {
        boolean isServerPlayerEntity = playerIn instanceof EntityPlayerMPFake;
        if (isServerPlayerEntity)
        {
            return new NetHandlerPlayServerFake(this.server, networkManager, playerIn);
        }
        else
        {
            return new ServerGamePacketListenerImpl(this.server, networkManager, playerIn);
        }
    }

    @Redirect(method = "getPlayerForLogin", at = @At(value = "INVOKE", target = "Ljava/util/Iterator;hasNext()Z"))
    private boolean cancelWhileLoop(Iterator<ServerPlayer> iterator)
    {
        return false;
    }

    @Inject(method = "getPlayerForLogin", at = @At(value = "INVOKE", shift = At.Shift.BEFORE,
            target = "Ljava/util/Iterator;hasNext()Z"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void newWhileLoop(GameProfile gameProfile_1, CallbackInfoReturnable<ServerPlayer> cir, UUID uUID_1,
                              List<ServerPlayer> list_1, ServerPlayer serverPlayerEntity, Iterator<ServerPlayer> var5)
    {
        while (var5.hasNext())
        {
            ServerPlayer serverPlayerEntity_3 = var5.next();
            if(serverPlayerEntity_3 instanceof EntityPlayerMPFake)
            {
                ((EntityPlayerMPFake)serverPlayerEntity_3).kill(new TranslatableComponent("multiplayer.disconnect.duplicate_login"));
                continue;
            }
            serverPlayerEntity_3.connection.disconnect(new TranslatableComponent("multiplayer.disconnect.duplicate_login"));
        }
    }
}
