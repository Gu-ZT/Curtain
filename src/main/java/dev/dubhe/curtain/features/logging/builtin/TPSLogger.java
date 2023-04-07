package dev.dubhe.curtain.features.logging.builtin;

import dev.dubhe.curtain.Curtain;
import dev.dubhe.curtain.features.logging.AbstractHudLogger;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.Arrays;
import java.util.OptionalDouble;

public class TPSLogger extends AbstractHudLogger {
    private static final double MAX_TPS = 20d;

    public TPSLogger() {
        super("tps");
    }

    @Override
    public ITextComponent display(ServerPlayerEntity player) {
        MinecraftServer server = Curtain.minecraftServer;
        final OptionalDouble averageTPS = Arrays.stream(server.tickTimes).average();
        if (averageTPS.isEmpty()) {
            return new StringTextComponent("No TPS data available").withStyle(TextFormatting.RED);
        }
        double MSPT = Arrays.stream(server.tickTimes).average().getAsDouble() * 1.0E-6D;
        double TPS = Math.min(1000.0D / MSPT, MAX_TPS);
        TextFormatting color = TextFormatting.GREEN;
        if (MSPT >= 0.0D) {
            color = TextFormatting.DARK_GREEN;
        }
        if (MSPT >= 20D) {
            color = TextFormatting.GREEN;
        }
        if (MSPT >= 35) {
            color = TextFormatting.YELLOW;
        }
        if (MSPT >= 45) {
            color = TextFormatting.RED;
        }
        TextFormatting finalColor = color;
        return new StringTextComponent("TPS: ").withStyle(TextFormatting.GRAY)
                .append(new StringTextComponent("%.1f".formatted(TPS)).withStyle(finalColor))
                .append(new StringTextComponent(" MSPT: ").withStyle(TextFormatting.GRAY))
                .append(new StringTextComponent("%.1f".formatted(MSPT)).withStyle(finalColor));
    }
}
