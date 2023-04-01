package dev.dubhe.curtain.features.logging;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraftforge.event.level.LevelEvent;
import org.jetbrains.annotations.NotNull;

public class MobcapsLogger extends AbstractLogger {
    public static final int MAGIC_NUMBER = (int)Math.pow(17.0D, 2.0D);
    public MobcapsLogger() {
        super("mobcaps", "dynamic", "dynamic", "overworld", "nether", "end");
    }

    public void a(LevelEvent event){}

    @Override
    public Component get(@NotNull ServerPlayer player) {
        MutableComponent main = Component.empty();
        ServerLevel level = player.getLevel();
        NaturalSpawner.SpawnState lastSpawner = level.getChunkSource().getLastSpawnState();
        if (null == lastSpawner) return main;
        Object2IntMap<MobCategory> dimCounts = lastSpawner.getMobCategoryCounts();
        for (MobCategory category : MobCategory.values()) {
            int cur = dimCounts.getOrDefault(category, -1);
            //int max = (int)(chunkcount * ((double)category.getMaxInstancesPerChunk() / MAGIC_NUMBER));
        }
        return main;
    }
}
