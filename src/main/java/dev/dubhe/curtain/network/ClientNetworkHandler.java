package dev.dubhe.curtain.network;

import dev.dubhe.curtain.CurtainRules;
import dev.dubhe.curtain.features.rules.fakes.LevelInterface;
import dev.dubhe.curtain.utils.TickRateManager;
import io.netty.buffer.Unpooled;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class ClientNetworkHandler
{
    private static final Map<String, BiConsumer<LocalPlayer, Tag>> dataHandlers = new HashMap<String, BiConsumer<LocalPlayer, Tag>>();
    static
    {
        dataHandlers.put("TickRate", (p, t) -> {
            TickRateManager tickRateManager = ((LevelInterface)p.clientLevel).tickRateManager();
            tickRateManager.setTickRate(((NumericTag) t).getAsFloat());
        });
        dataHandlers.put("TickingState", (p, t) -> {
            CompoundTag tickingState = (CompoundTag)t;
            TickRateManager tickRateManager = ((LevelInterface)p.clientLevel).tickRateManager();
            tickRateManager.setFrozenState(tickingState.getBoolean("is_paused"), tickingState.getBoolean("deepFreeze"));
        });
        dataHandlers.put("SuperHotState", (p, t) -> {
            TickRateManager tickRateManager = ((LevelInterface)p.clientLevel).tickRateManager();
            tickRateManager.setSuperHot(((ByteTag) t).equals(ByteTag.ONE));
        });
        dataHandlers.put("TickPlayerActiveTimeout", (p, t) -> {
            TickRateManager tickRateManager = ((LevelInterface)p.clientLevel).tickRateManager();
            tickRateManager.setPlayerActiveTimeout(((NumericTag) t).getAsInt());
        });
        dataHandlers.put("clientCommand", (p, t) -> {
            CurtainClient.onClientCommand(t);
        });
    };

    // Ran on the Main Minecraft Thread
    public static void handleData(FriendlyByteBuf data, LocalPlayer player)
    {
        if (data != null)
        {
            int id = data.readVarInt();
            if (id == CurtainClient.HI)
                onHi(data);
            if (id == CurtainClient.DATA)
                onSyncData(data, player);
        }
    }

    private static void onHi(FriendlyByteBuf data)
    {
        CurtainClient.setCarpet();
        CurtainClient.serverCurtainVersion = data.readUtf(64);
        if (CurtainRules.curtainVersion.equals(CurtainClient.serverCurtainVersion))
        {
            CurtainRules.LOG.info("Joined carpet server with matching carpet version");
        }
        else
        {
            CurtainRules.LOG.warn("Joined carpet server with another carpet version: "+CurtainClient.serverCurtainVersion);
        }
        // We can ensure that this packet is
        // processed AFTER the player has joined
        respondHello();
    }

    public static void respondHello()
    {
        CurtainClient.getPlayer().connection.send(new ServerboundCustomPayloadPacket(
                CurtainClient.CURTAIN_CHANNEL,
                (new FriendlyByteBuf(Unpooled.buffer())).writeVarInt(CurtainClient.HELLO).writeUtf(CurtainRules.curtainVersion)
        ));
    }

    private static void onSyncData(FriendlyByteBuf data, LocalPlayer player)
    {
        CompoundTag compound = data.readNbt();
        if (compound == null) return;
        for (String key: compound.getAllKeys())
        {
            if (dataHandlers.containsKey(key)) {
                try {
                    dataHandlers.get(key).accept(player, compound.get(key));
                }
                catch (Exception exc)
                {
                    CurtainRules.LOG.info("Corrupt carpet data for "+key);
                }
            }
            else
                CurtainRules.LOG.error("Unknown carpet data: "+key);
        }
    }

    public static void clientCommand(String command)
    {
        CompoundTag tag = new CompoundTag();
        tag.putString("id", command);
        tag.putString("command", command);
        CompoundTag outer = new CompoundTag();
        outer.put("clientCommand", tag);
        CurtainClient.getPlayer().connection.send(new ServerboundCustomPayloadPacket(
                CurtainClient.CURTAIN_CHANNEL,
                (new FriendlyByteBuf(Unpooled.buffer())).writeVarInt(CurtainClient.DATA).writeNbt(outer)
        ));
    }
}
