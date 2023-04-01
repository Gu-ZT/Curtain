package dev.dubhe.curtain.fakes;

import io.netty.channel.Channel;

public interface ClientConnectionInterface {
    void setChannel(Channel channel);
}
