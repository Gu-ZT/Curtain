package dev.dubhe.curtain.features.logging.builtin;

import dev.dubhe.curtain.features.logging.AbstractHudLogger;
import dev.dubhe.curtain.utils.SpawnReporter;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class MobcapsLogger extends AbstractHudLogger {

    public MobcapsLogger() {
        super("mobcaps");
    }

    @Override
    public ITextComponent display(ServerPlayerEntity player) {
        RegistryKey<World> dim = player.level.dimension();
        ITextComponent msg = SpawnReporter.printMobcapsForDimension(player.getServer().getLevel(dim), false).get(0);
        return msg;
    }
}
