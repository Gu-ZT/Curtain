package dev.dubhe.curtain.features.player.helpers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.dubhe.curtain.CurtainRules;
import dev.dubhe.curtain.features.player.patches.EntityPlayerMPFake;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.storage.LevelResource;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

import static dev.dubhe.curtain.features.player.menu.MenuHashMap.FAKE_PLAYER_INVENTORY_MENU_MAP;

public class FakePlayerResident {
    public static void onServerStop(MinecraftServer server) {
        if (CurtainRules.fakePlayerResident) {
            JsonObject fakePlayerList = new JsonObject();
            FAKE_PLAYER_INVENTORY_MENU_MAP.forEach((player, fakePlayerInventoryContainer) -> {
                if (!(player instanceof EntityPlayerMPFake)) return;
                String username = player.getName().getString();
                fakePlayerList.add(username, FakePlayerResident.save(player));
            });
            File file = server.getWorldPath(LevelResource.ROOT).resolve("fake_player.gca.json").toFile();
            if (!file.isFile()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try (BufferedWriter bfw = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
                bfw.write(new Gson().toJson(fakePlayerList));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FAKE_PLAYER_INVENTORY_MENU_MAP.clear();
    }

    public static void onServerStart(MinecraftServer server) {
        if (CurtainRules.fakePlayerResident) {
            JsonObject fakePlayerList = new JsonObject();
            File file = server.getWorldPath(LevelResource.ROOT).resolve("fake_player.gca.json").toFile();
            if (!file.isFile()) {
                return;
            }
            try (BufferedReader bfr = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
                fakePlayerList = new Gson().fromJson(bfr, JsonObject.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (Map.Entry<String, JsonElement> entry : fakePlayerList.entrySet()) {
                FakePlayerResident.load(entry, server);
            }
            file.delete();
        }
    }

    public static JsonObject save(Player player) {
        double pos_x = player.getX();
        double pos_y = player.getY();
        double pos_z = player.getZ();
        double yaw = player.getYRot();
        double pitch = player.getXRot();
        String dimension = player.level().dimension().location().getPath();
        String gamemode = ((ServerPlayer) player).gameMode.getGameModeForPlayer().getName();
        boolean flying = player.getAbilities().flying;
        JsonObject fakePlayer = new JsonObject();
        fakePlayer.addProperty("pos_x", pos_x);
        fakePlayer.addProperty("pos_y", pos_y);
        fakePlayer.addProperty("pos_z", pos_z);
        fakePlayer.addProperty("yaw", yaw);
        fakePlayer.addProperty("pitch", pitch);
        fakePlayer.addProperty("dimension", dimension);
        fakePlayer.addProperty("gamemode", gamemode);
        fakePlayer.addProperty("flying", flying);
        return fakePlayer;
    }

    public static void load(Map.Entry<String, JsonElement> entry, MinecraftServer server) {
        String username = entry.getKey();
        JsonObject fakePlayer = entry.getValue().getAsJsonObject();
        double pos_x = fakePlayer.get("pos_x").getAsDouble();
        double pos_y = fakePlayer.get("pos_y").getAsDouble();
        double pos_z = fakePlayer.get("pos_z").getAsDouble();
        double yaw = fakePlayer.get("yaw").getAsDouble();
        double pitch = fakePlayer.get("pitch").getAsDouble();
        String dimension = fakePlayer.get("dimension").getAsString();
        String gamemode = fakePlayer.get("gamemode").getAsString();
        boolean flying = fakePlayer.get("flying").getAsBoolean();
        EntityPlayerMPFake.createFakePlayer(username, server, pos_x, pos_y, pos_z, yaw, pitch,
                ResourceKey.create(Registries.DIMENSION, new ResourceLocation(dimension)),
                GameType.byName(gamemode), flying);
    }
}
