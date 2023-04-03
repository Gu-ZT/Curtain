package dev.dubhe.curtain.features.logging.builtin;

import dev.dubhe.curtain.Curtain;
import dev.dubhe.curtain.features.logging.AbstractHudLogger;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.Arrays;
import java.util.OptionalDouble;

public class TPSLogger extends AbstractHudLogger {
    private static final double MAX_TPS = 20d;

    public TPSLogger() {
        super("tps");
    }

    @Override
    public Component display(ServerPlayer player) {
        MinecraftServer server = Curtain.minecraftServer;
        final OptionalDouble averageTPS = Arrays.stream(server.tickTimes).average();
        if (averageTPS.isEmpty()) {
            return Component.literal("No TPS data available").withStyle(style -> style.withColor(ChatFormatting.RED));
        }
        double MSPT = Arrays.stream(server.tickTimes).average().getAsDouble() * 1.0E-6D;
        double TPS = Math.min(1000.0D / MSPT, MAX_TPS);
        ChatFormatting color = ChatFormatting.GREEN;
        if (MSPT >= 0.0D) {
            color = ChatFormatting.DARK_GREEN;
        }
        if (MSPT >= 20D) {
            color = ChatFormatting.GREEN;
        }
        if (MSPT >= 35) {
            color = ChatFormatting.YELLOW;
        }
        if (MSPT >= 45) {
            color = ChatFormatting.RED;
        }
        ChatFormatting finalColor = color;
        return Component.literal("TPS: ").withStyle(style -> style.withColor(ChatFormatting.GRAY))
                .append(Component.literal("%.1f".formatted(TPS)).withStyle(style -> style.withColor(finalColor)))
                .append(Component.literal(" MSPT: ").withStyle(style -> style.withColor(ChatFormatting.GRAY)))
                .append(Component.literal("%.1f".formatted(MSPT)).withStyle(style -> style.withColor(finalColor)));
    }
}
