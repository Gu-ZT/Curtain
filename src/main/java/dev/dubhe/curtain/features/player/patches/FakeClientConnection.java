package dev.dubhe.curtain.features.player.patches;

import dev.dubhe.curtain.features.player.fakes.IClientConnection;
import io.netty.channel.embedded.EmbeddedChannel;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;

import javax.annotation.Nullable;

public class FakeClientConnection extends Connection {
    public FakeClientConnection(PacketFlow packetFlow) {
        super(packetFlow);
        ((IClientConnection) this).setChannel(new EmbeddedChannel());

    }

    @Override
    public void setReadOnly() {
    }

    @Override
    public void send(Packet<?> pPacket, @Nullable PacketSendListener pSendListener) {
    }


    @Override
    public void handleDisconnection() {
    }
}
