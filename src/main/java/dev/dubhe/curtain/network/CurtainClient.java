package dev.dubhe.curtain.network;

import dev.dubhe.curtain.Curtain;
import dev.dubhe.curtain.CurtainRules;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class CurtainClient {
    public static final int HI = 69;
    public static final int HELLO = 420;
    public static final int DATA = 1;

    private static LocalPlayer clientPlayer = null;
    private static boolean isServerCurtain = false;
    public static String serverCurtainVersion;
    public static final ResourceLocation CURTAIN_CHANNEL = new ResourceLocation("curtain:hello");

    public static void gameJoined(LocalPlayer player)
    {
        clientPlayer = player;
    }

    public static void disconnect()
    {
        if (isServerCurtain) // multiplayer connection
        {
            isServerCurtain = false;
            clientPlayer = null;
            Curtain.onServerClosed(null);
        }
    }

    public static void setCarpet()
    {
        isServerCurtain = true;
    }

    public static LocalPlayer getPlayer()
    {
        return clientPlayer;
    }

    public static boolean isCarpet()
    {
        return isServerCurtain;
    }

    public static boolean sendClientCommand(String command)
    {
        if (!isServerCurtain && Curtain.minecraftServer == null) return false;
        ClientNetworkHandler.clientCommand(command);
        return true;
    }

    public static void onClientCommand(Tag t)
    {
        CurtainRules.LOG.info("Server Response:");
        CompoundTag tag = (CompoundTag)t;
        CurtainRules.LOG.info(" - id: "+tag.getString("id"));
        CurtainRules.LOG.info(" - code: "+tag.getInt("code"));
        if (tag.contains("error")) CurtainRules.LOG.warn(" - error: "+tag.getString("error"));
        if (tag.contains("output"))
        {
            ListTag outputTag = (ListTag) tag.get("output");
            for (int i = 0; i < outputTag.size(); i++)
                CurtainRules.LOG.info(" - response: " + Component.Serializer.fromJson(outputTag.getString(i)).getString());
        }
    }
}
