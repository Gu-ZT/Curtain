package dev.dubhe.curtain.features.logging;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

public class TpsLogger extends AbstractLogger {
    private double mspt = 20;
    private double tps = 20;

    public TpsLogger() {
        super("tps", "20.00");
    }

    @SubscribeEvent
    public void change(TickEvent.ServerTickEvent event) {
        MinecraftServer server = event.getServer();
        this.mspt = Mth.average(server.tickTimes) * 1.0E-6D;
        this.tps = Math.min(1000.0D / this.mspt, 20.0);
    }

    @Override
    public Component get(@NotNull ServerPlayer player) {
        MutableComponent main = Component.empty();
        MutableComponent tps = Component.literal("TPS: %.2f/%s".formatted(this.tps, this.defaultValue));
        if (this.tps < 20.0) tps.withStyle(ChatFormatting.RED);
        else tps.withStyle(ChatFormatting.GREEN);
        MutableComponent mspt = Component.literal("MSPT: %.2f/%s".formatted(this.mspt, "50.00"));
        if (this.mspt > 50.0) mspt.withStyle(ChatFormatting.RED);
        else mspt.withStyle(ChatFormatting.GREEN);
        main.append(tps).append("\n").append(mspt);
        return main;
    }
}
