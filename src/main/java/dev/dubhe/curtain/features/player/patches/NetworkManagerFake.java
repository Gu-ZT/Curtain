package dev.dubhe.curtain.features.player.patches;

import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketDirection;

public class NetworkManagerFake extends NetworkManager
{
    public NetworkManagerFake(PacketDirection p)
    {
        super(p);
    }

    @Override
    public void setReadOnly()
    {
    }

    @Override
    public void handleDisconnection()
    {
    }
}