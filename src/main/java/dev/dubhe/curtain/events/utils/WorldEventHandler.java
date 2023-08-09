package dev.dubhe.curtain.events.utils;

import dev.dubhe.curtain.Curtain;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

public class WorldEventHandler {
    @SubscribeEvent
    public void onLevelSave(@NotNull WorldEvent.Save event) {
        Curtain.rules.saveToFile();
    }
}
