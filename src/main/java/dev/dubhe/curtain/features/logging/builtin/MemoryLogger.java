package dev.dubhe.curtain.features.logging.builtin;

import dev.dubhe.curtain.features.logging.AbstractHudLogger;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;

public class MemoryLogger extends AbstractHudLogger {

    public MemoryLogger() {
        super("memory");
    }

    @Override
    public Component display(ServerPlayer player) {
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        long usedMemory = totalMemory - freeMemory;
        MutableComponent msg = new TextComponent("");
        msg.append(new TextComponent("%.1f".formatted(usedMemory / 1024 / 1024f) + " M")
                .withStyle(ChatFormatting.GRAY));
        msg.append(new TextComponent(" / ").withStyle(ChatFormatting.WHITE));
        msg.append(new TextComponent("%.1f".formatted(totalMemory / 1024 / 1024f) + " M")
                .withStyle(ChatFormatting.GRAY));
        return msg;
    }
}
