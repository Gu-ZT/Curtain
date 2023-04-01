package dev.dubhe.curtain.mixins;

import dev.dubhe.curtain.fakes.ClientConnectionInterface;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public abstract class ClientConnectionMixin implements ClientConnectionInterface {
    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/protocol/Packet;)V",at = @At("HEAD"))
    private void packetInCount(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo ci){
    }

    @Override
    @Accessor
    public abstract void setChannel(Channel channel);
}
