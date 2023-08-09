package dev.dubhe.curtain.features.player.patches;

import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

@SuppressWarnings("NullableProblems")
public class NetHandlerPlayServerFake extends ServerGamePacketListenerImpl {
    public NetHandlerPlayServerFake(MinecraftServer server, Connection cc, ServerPlayer playerIn) {
        super(server, cc, playerIn);
    }

    @Override
    public void send(final Packet<?> packet) {
    }

    @Override
    public void disconnect(Component message) {
        if (player instanceof EntityPlayerMPFake && message instanceof TranslatableComponent && ((TranslatableComponent) message).getKey().equals("multiplayer.disconnect.idling")) {
            ((EntityPlayerMPFake) player).kill(new TranslatableComponent(((TranslatableComponent) message).getKey()));
        }
    }
}



