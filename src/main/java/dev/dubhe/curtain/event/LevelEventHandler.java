package dev.dubhe.curtain.event;

import dev.dubhe.curtain.Curtain;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

public class LevelEventHandler {
    @SubscribeEvent
    public void onLevelSave(@NotNull LevelEvent.Save event) {
        Curtain.manager.saveToFile();
    }
}
