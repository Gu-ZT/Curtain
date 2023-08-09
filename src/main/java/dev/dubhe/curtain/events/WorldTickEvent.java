package dev.dubhe.curtain.events;

import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.WorldEvent;

public class WorldTickEvent extends WorldEvent {
    public WorldTickEvent(ServerWorld world) {
        super(world);
    }
}
