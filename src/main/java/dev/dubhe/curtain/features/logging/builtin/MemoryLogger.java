package dev.dubhe.curtain.features.logging.builtin;

import dev.dubhe.curtain.features.logging.AbstractHudLogger;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class MemoryLogger extends AbstractHudLogger {

    public MemoryLogger() {
        super("memory");
    }

    @Override
    public ITextComponent display(ServerPlayerEntity player) {
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        long usedMemory = totalMemory - freeMemory;
        IFormattableTextComponent msg = new StringTextComponent("");
        msg.append(new StringTextComponent("%.1f".formatted(usedMemory / 1024 / 1024f) + " M")
                        .withStyle(TextFormatting.GRAY));
        msg.append(new StringTextComponent(" / ").withStyle(TextFormatting.WHITE));
        msg.append(new StringTextComponent("%.1f".formatted(totalMemory / 1024 / 1024f) + " M")
                .withStyle(TextFormatting.GRAY));
        return msg;
    }
}
