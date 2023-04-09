package dev.dubhe.curtain.mixins.rules.command_player;

import com.mojang.authlib.GameProfile;
import dev.dubhe.curtain.features.player.patches.EntityPlayerMPFake;
import dev.dubhe.curtain.features.player.patches.NetHandlerPlayServerFake;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.ServerPlayNetHandler;
import net.minecraft.server.MinecraftServer;

import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.TranslationTextComponent;
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
public abstract class PlayerListMixin
{
    @Shadow
    @Final
    private MinecraftServer server;

    @Inject(method = "load", at = @At(value = "RETURN", shift = At.Shift.BEFORE))
    private void fixStartingPos(ServerPlayerEntity serverPlayerEntity_1, CallbackInfoReturnable<CompoundNBT> cir)
    {
        if (serverPlayerEntity_1 instanceof EntityPlayerMPFake)
        {
            ((EntityPlayerMPFake) serverPlayerEntity_1).fixStartingPosition.run();
        }
    }

    @Redirect(method = "placeNewPlayer", at = @At(value = "NEW", target = "net/minecraft/network/play/ServerPlayNetHandler"))
    private ServerPlayNetHandler replaceNetworkHandler(MinecraftServer server, NetworkManager networkManager, ServerPlayerEntity playerIn)
    {
        boolean isServerPlayerEntity = playerIn instanceof EntityPlayerMPFake;
        if (isServerPlayerEntity)
        {
            return new NetHandlerPlayServerFake(this.server, networkManager, playerIn);
        }
        else
        {
            return new ServerPlayNetHandler(this.server, networkManager, playerIn);
        }
    }

    @Redirect(method = "getPlayerForLogin", at = @At(value = "INVOKE", target = "Ljava/util/Iterator;hasNext()Z"))
    private boolean cancelWhileLoop(Iterator iterator)
    {
        return false;
    }

    @Inject(method = "getPlayerForLogin", at = @At(value = "INVOKE", shift = At.Shift.BEFORE,
            target = "Ljava/util/Iterator;hasNext()Z"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void newWhileLoop(GameProfile gameProfile_1, CallbackInfoReturnable<ServerPlayerEntity> cir, UUID uUID_1,
                              List list_1, ServerPlayerEntity serverPlayerEntity, Iterator var5)
    {
        while (var5.hasNext())
        {
            ServerPlayerEntity serverPlayerEntity_3 = (ServerPlayerEntity) var5.next();
            if(serverPlayerEntity_3 instanceof EntityPlayerMPFake)
            {
                ((EntityPlayerMPFake)serverPlayerEntity_3).kill(new TranslationTextComponent("multiplayer.disconnect.duplicate_login"));
                continue;
            }
            serverPlayerEntity_3.connection.disconnect(new TranslationTextComponent("multiplayer.disconnect.duplicate_login"));
        }
    }

}
