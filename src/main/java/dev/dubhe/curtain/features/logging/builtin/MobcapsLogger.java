package dev.dubhe.curtain.features.logging.builtin;

import dev.dubhe.curtain.features.logging.AbstractHudLogger;
import dev.dubhe.curtain.utils.SpawnReporter;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public class MobcapsLogger extends AbstractHudLogger {

    public MobcapsLogger() {
        super("mobcaps");
    }

    @Override
    public Component display(ServerPlayer player) {
        ResourceKey<Level> dim = player.level().dimension();
        Component msg = SpawnReporter.printMobcapsForDimension(player.getServer().getLevel(dim), false).get(0);
        return msg;
    }
}
