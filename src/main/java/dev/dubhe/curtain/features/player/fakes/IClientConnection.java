package dev.dubhe.curtain.features.player.fakes;

import io.netty.channel.Channel;

public interface IClientConnection {
    void setChannel(Channel channel);
}
