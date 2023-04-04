package dev.dubhe.curtain.mixins;

import dev.dubhe.curtain.features.player.fakes.IClientConnection;
import io.netty.channel.Channel;
import net.minecraft.network.Connection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Connection.class)
public abstract class ClientConnectionMixin implements IClientConnection {
    @Override
    @Accessor
    public abstract void setChannel(Channel channel);
}
