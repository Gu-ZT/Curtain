package dev.dubhe.curtain.features.logging;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class LoggerManager {
    private Set<String> loggingOptions = new HashSet<>();
    private final List<AbstractLogger> loggers = new ArrayList<>();

    public LoggerManager() {
        this.add(new TpsLogger());
        this.add(new MobcapsLogger());
    }

    public void add(AbstractLogger logger) {
        MinecraftForge.EVENT_BUS.register(logger);
        loggers.add(logger);
    }

    public void destroy() {
        for (AbstractLogger logger : loggers) {
            MinecraftForge.EVENT_BUS.unregister(logger);
        }
    }

    public void change(@NotNull String options) {
        this.loggingOptions = new HashSet<>(Arrays.asList(options.split(",")));
    }

    public MutableComponent display(ServerPlayer player) {
        MutableComponent main = Component.empty();
        if (loggingOptions.contains(null)) return main;
        main.append("=====================").withStyle(ChatFormatting.GRAY);
        for (AbstractLogger logger : loggers) {
            if (!loggingOptions.contains(logger.getName())) continue;
            main.append("\n");
            main.append(logger.get(player));
        }
        return main;
    }
}
