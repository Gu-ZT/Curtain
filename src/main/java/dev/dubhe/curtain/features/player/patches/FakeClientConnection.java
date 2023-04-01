package dev.dubhe.curtain.features.player.patches;

import dev.dubhe.curtain.features.player.fakes.IClientConnection;
import io.netty.channel.embedded.EmbeddedChannel;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;

public class FakeClientConnection extends Connection {
    public FakeClientConnection(PacketFlow packetFlow) {
        super(packetFlow);
        ((IClientConnection)this).setChannel(new EmbeddedChannel());
    }

    @Override
    public void setReadOnly() {
    }

    @Override
    public void handleDisconnection() {
    }
}
