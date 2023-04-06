package dev.dubhe.curtain.features.player.patches;

import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketDirection;

public class FakeClientConnection extends NetworkManager {
    public FakeClientConnection(PacketDirection p) {
        super(p);
    }

    @Override
    public void setReadOnly() {
    }

    @Override
    public void handleDisconnection() {
    }

}
