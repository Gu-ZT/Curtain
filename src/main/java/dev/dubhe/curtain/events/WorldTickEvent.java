package dev.dubhe.curtain.events;

import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.world.WorldEvent;

public class WorldTickEvent extends WorldEvent {
    public WorldTickEvent(LevelAccessor world) {
        super(world);
    }
}
