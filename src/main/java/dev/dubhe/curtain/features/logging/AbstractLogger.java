package dev.dubhe.curtain.features.logging;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractLogger {
    protected final String name;
    protected final String defaultValue;
    protected final List<String> options = new ArrayList<>();

    public AbstractLogger(String name, String defaultValue, String... options) {
        this.name = name;
        this.defaultValue = defaultValue;
        Collections.addAll(this.options, options);
    }

    public String getName() {
        return this.name;
    }

    public abstract Component get(@NotNull ServerPlayer player);
}
