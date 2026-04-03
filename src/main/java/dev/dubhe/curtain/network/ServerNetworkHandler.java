package dev.dubhe.curtain.network;

import dev.dubhe.curtain.CurtainRules;
import dev.dubhe.curtain.api.rules.CurtainRule;
import dev.dubhe.curtain.api.rules.RuleHelper;
import dev.dubhe.curtain.features.rules.fakes.MinecraftServerInterface;
import dev.dubhe.curtain.features.rules.fakes.ServerGamePacketListenerImplInterface;
import dev.dubhe.curtain.utils.ServerTickRateManager;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.*;
import java.util.function.BiConsumer;

public class ServerNetworkHandler
{
    private static final Map<ServerPlayer, String> remoteCarpetPlayers = new HashMap<>();
    private static final Set<ServerPlayer> validCarpetPlayers = new HashSet<>();

    private static final Map<String, BiConsumer<ServerPlayer, Tag>> dataHandlers = Map.of(
            "clientCommand", (p, t) -> {
                handleClientCommand(p, (CompoundTag)t);
            }
    );

    public static void handleData(FriendlyByteBuf data, ServerPlayer player)
    {
        if (data != null)
        {
            int id = data.readVarInt();
            if (id == CurtainClient.HELLO)
                onHello(player, data);
            if (id == CurtainClient.DATA)
                onClientData(player, data);
        }
    }



    public static void onPlayerJoin(ServerPlayer playerEntity)
    {
        if (!((ServerGamePacketListenerImplInterface)playerEntity.connection).getConnection().isMemoryConnection())
        {
            playerEntity.connection.send(new ClientboundCustomPayloadPacket(
                    CurtainClient.CURTAIN_CHANNEL,
                    (new FriendlyByteBuf(Unpooled.buffer())).writeVarInt(CurtainClient.HI).writeUtf(CurtainRules.curtainVersion)
            ));
        }
        else
        {
            validCarpetPlayers.add(playerEntity);
        }

    }

    public static void onHello(ServerPlayer playerEntity, FriendlyByteBuf packetData)
    {
        validCarpetPlayers.add(playerEntity);
        String clientVersion = packetData.readUtf(64);
        remoteCarpetPlayers.put(playerEntity, clientVersion);
        if (clientVersion.equals(CurtainRules.curtainVersion))
            CurtainRules.LOG.info("Player "+playerEntity.getName().getString()+" joined with a matching carpet client");
        else
            CurtainRules.LOG.warn("Player "+playerEntity.getName().getString()+" joined with another carpet version: "+clientVersion);

        DataBuilder data = DataBuilder.create(playerEntity.server);//;.withTickRate().withFrozenState().withTickPlayerActiveTimeout(); // .withSuperHotState()
        playerEntity.connection.send(new ClientboundCustomPayloadPacket(CurtainClient.CURTAIN_CHANNEL, data.build()));
    }

    public static void sendPlayerLevelData(ServerPlayer player, ServerLevel level) {
        DataBuilder data = DataBuilder.create(player.server).withTickRate().withFrozenState().withTickPlayerActiveTimeout(); // .withSuperHotState()
        player.connection.send(new ClientboundCustomPayloadPacket(CurtainClient.CURTAIN_CHANNEL, data.build() ));

    }

    private static void handleClientCommand(ServerPlayer player, CompoundTag commandData)
    {
        String command = commandData.getString("command");
        String id = commandData.getString("id");
        List<Component> output = new ArrayList<>();
        Component[] error = {null};
        int resultCode = -1;
        CompoundTag result = new CompoundTag();
        result.putString("id", id);
        result.putInt("code", resultCode);
        if (error[0] != null) result.putString("error", error[0].getContents().toString());
        ListTag outputResult = new ListTag();
        for (Component line: output) outputResult.add(StringTag.valueOf(Component.Serializer.toJson(line)));
        if (!output.isEmpty()) result.put("output", outputResult);
        player.connection.send(new ClientboundCustomPayloadPacket(
                CurtainClient.CURTAIN_CHANNEL,
                DataBuilder.create(player.server).withCustomNbt("clientCommand", result).build()
        ));
        // run command plug to command output,
    }


    private static void onClientData(ServerPlayer player, FriendlyByteBuf data)
    {
        CompoundTag compound = data.readNbt();
        if (compound == null) return;
        for (String key: compound.getAllKeys())
        {
            if (dataHandlers.containsKey(key))
                dataHandlers.get(key).accept(player, compound.get(key));
            else
                CurtainRules.LOG.warn("Unknown carpet client data: "+key);
        }
    }

    public static void updateRuleWithConnectedClients(CurtainRule<?> rule)
    {
        if (CurtainRules.superSecretSetting) return;
        for (ServerPlayer player : remoteCarpetPlayers.keySet())
        {
            player.connection.send(new ClientboundCustomPayloadPacket(
                    CurtainClient.CURTAIN_CHANNEL,
                    DataBuilder.create(player.server).withRule(rule).build()
            ));
        }
    }

    public static void updateTickSpeedToConnectedPlayers(MinecraftServer server)
    {
        if (CurtainRules.superSecretSetting) return;
        for (ServerPlayer player : validCarpetPlayers)
        {
            player.connection.send(new ClientboundCustomPayloadPacket(
                    CurtainClient.CURTAIN_CHANNEL,
                    DataBuilder.create(player.server).withTickRate().build()
            ));
        }
    }

    public static void updateFrozenStateToConnectedPlayers(MinecraftServer server)
    {
        if (CurtainRules.superSecretSetting) return;
        for (ServerPlayer player : validCarpetPlayers)
        {
            player.connection.send(new ClientboundCustomPayloadPacket(
                    CurtainClient.CURTAIN_CHANNEL,
                    DataBuilder.create(player.server).withFrozenState().build()
            ));
        }
    }

    public static void updateSuperHotStateToConnectedPlayers(MinecraftServer server)
    {
        if(CurtainRules.superSecretSetting) return;
        for (ServerPlayer player : validCarpetPlayers)
        {
            player.connection.send(new ClientboundCustomPayloadPacket(
                    CurtainClient.CURTAIN_CHANNEL,
                    DataBuilder.create(player.server).withSuperHotState().build()
            ));
        }
    }

    public static void updateTickPlayerActiveTimeoutToConnectedPlayers(MinecraftServer server)
    {
        if (CurtainRules.superSecretSetting) return;
        for (ServerPlayer player : validCarpetPlayers)
        {
            player.connection.send(new ClientboundCustomPayloadPacket(
                    CurtainClient.CURTAIN_CHANNEL,
                    DataBuilder.create(player.server).withTickPlayerActiveTimeout().build()
            ));
        }
    }

    public static void broadcastCustomCommand(String command, Tag data)
    {
        if (CurtainRules.superSecretSetting) return;
        for (ServerPlayer player : validCarpetPlayers)
        {
            player.connection.send(new ClientboundCustomPayloadPacket(
                    CurtainClient.CURTAIN_CHANNEL,
                    DataBuilder.create(player.server).withCustomNbt(command, data).build()
            ));
        }
    }

    public static void sendCustomCommand(ServerPlayer player, String command, Tag data)
    {
        if (isValidCarpetPlayer(player))
        {
            player.connection.send(new ClientboundCustomPayloadPacket(
                    CurtainClient.CURTAIN_CHANNEL,
                    DataBuilder.create(player.server).withCustomNbt(command, data).build()
            ));
        }
    }


    public static void onPlayerLoggedOut(ServerPlayer player)
    {
        validCarpetPlayers.remove(player);
        if (!((ServerGamePacketListenerImplInterface)player.connection).getConnection().isMemoryConnection())
            remoteCarpetPlayers.remove(player);
    }

    public static void close()
    {
        remoteCarpetPlayers.clear();
        validCarpetPlayers.clear();
    }

    public static boolean isValidCarpetPlayer(ServerPlayer player)
    {
        if (CurtainRules.superSecretSetting) return false;
        return validCarpetPlayers.contains(player);

    }

    public static String getPlayerStatus(ServerPlayer player)
    {
        if (remoteCarpetPlayers.containsKey(player)) return "carpet "+remoteCarpetPlayers.get(player);
        if (validCarpetPlayers.contains(player)) return "carpet "+CurtainRules.curtainVersion;
        return "vanilla";
    }

    private static class DataBuilder
    {
        private CompoundTag tag;
        private MinecraftServer server;
        private static DataBuilder create(final MinecraftServer server)
        {
            return new DataBuilder(server);
        }
        private DataBuilder(MinecraftServer server)
        {
            tag = new CompoundTag();
            this.server = server;
        }
        private DataBuilder withTickRate()
        {
            ServerTickRateManager trm = ((MinecraftServerInterface)server).getTickRateManager();
            tag.putFloat("TickRate", trm.tickrate());
            return this;
        }
        private DataBuilder withFrozenState()
        {
            ServerTickRateManager trm = ((MinecraftServerInterface)server).getTickRateManager();
            CompoundTag tickingState = new CompoundTag();
            tickingState.putBoolean("is_paused", trm.gameIsPaused());
            tickingState.putBoolean("deepFreeze", trm.deeplyFrozen());
            tag.put("TickingState", tickingState);
            return this;
        }
        private DataBuilder withSuperHotState()
        {
            ServerTickRateManager trm = ((MinecraftServerInterface)server).getTickRateManager();
            tag.putBoolean("SuperHotState", trm.isSuperHot());
            return this;
        }
        private DataBuilder withTickPlayerActiveTimeout()
        {
            ServerTickRateManager trm = ((MinecraftServerInterface)server).getTickRateManager();
            tag.putInt("TickPlayerActiveTimeout", trm.getPlayerActiveTimeout());
            return this;
        }
        private DataBuilder withRule(CurtainRule<?> rule)
        {
            CompoundTag rules = (CompoundTag) tag.get("Rules");
            if (rules == null)
            {
                rules = new CompoundTag();
                tag.put("Rules", rules);
            }
            String key = rule.getName();
            while (rules.contains(key)) { key = key+"2";}
            CompoundTag ruleNBT = new CompoundTag();
            ruleNBT.putString("Value", RuleHelper.toRuleString(rule.getValue()));
            ruleNBT.putString("Rule", rule.getName());
            rules.put(key, ruleNBT);
            return this;
        }

        public DataBuilder withCustomNbt(String key, Tag value)
        {
            tag.put(key, value);
            return this;
        }

        private FriendlyByteBuf build()
        {
            FriendlyByteBuf packetBuf = new FriendlyByteBuf(Unpooled.buffer());
            packetBuf.writeVarInt(CurtainClient.DATA);
            packetBuf.writeNbt(tag);
            return packetBuf;
        }


    }
}
