package dev.dubhe.curtain.features.logging;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;

public abstract class AbstractLogger {
    private final String name;
    private final DisplayType type;
    public AbstractLogger(String name, DisplayType type) {
        this.name = name;
        this.type = type;
    }

    public AbstractLogger(String name) {
        this.name = name;
        this.type = DisplayType.CHAT;
    }
    public String getName() {
        return name;
    }

    public DisplayType getType() {
        return type;
    }

    public abstract ITextComponent display(ServerPlayerEntity player);
}
