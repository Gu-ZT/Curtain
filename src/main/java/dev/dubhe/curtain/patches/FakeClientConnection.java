package dev.dubhe.curtain.patches;

import dev.dubhe.curtain.fakes.ClientConnectionInterface;
import io.netty.channel.embedded.EmbeddedChannel;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;

public class FakeClientConnection extends Connection {
    public FakeClientConnection(PacketFlow packetFlow) {
        super(packetFlow);
        ((ClientConnectionInterface)this).setChannel(new EmbeddedChannel());
    }

    @Override
    public void setReadOnly() {
    }

    @Override
    public void handleDisconnection() {
    }
}
