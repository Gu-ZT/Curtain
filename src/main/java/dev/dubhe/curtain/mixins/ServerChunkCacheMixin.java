package dev.dubhe.curtain.mixins;

import dev.dubhe.curtain.utils.SpawnReporter;
import net.minecraft.entity.EntityClassification;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.TicketManager;
import net.minecraft.world.storage.IWorldInfo;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.HashSet;

@Mixin(ServerChunkProvider.class)
public abstract class ServerChunkCacheMixin {
    @Shadow
    @Final
    public ServerWorld level;

    @Shadow
    @Final
    private TicketManager distanceManager;

    @Redirect(
            method = "tickChunks",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/server/TicketManager;getNaturalSpawnChunkCount()I"
            )
    )
    //this runs once per world spawning cycle. Allows to grab mob counts and count spawn ticks
    private int setupTracking(TicketManager chunkTicketManager) {
        int j = chunkTicketManager.getNaturalSpawnChunkCount();
        RegistryKey<World> dim = this.level.dimension(); // getDimensionType;
        //((WorldInterface)world).getPrecookedMobs().clear(); not needed because mobs are compared with predefined BBs
        SpawnReporter.chunkCounts.put(dim, j);

        if (SpawnReporter.track_spawns > 0L) {
            //local spawns now need to be tracked globally cause each calll is just for chunk
            SpawnReporter.local_spawns = new HashMap<>();
            SpawnReporter.first_chunk_marker = new HashSet<>();
            for (EntityClassification cat : EntityClassification.values()) {
                Pair<RegistryKey<World>, EntityClassification> key = Pair.of(dim, cat);
                SpawnReporter.overall_spawn_ticks.put(key,
                        SpawnReporter.overall_spawn_ticks.get(key) +
                                SpawnReporter.spawn_tries.get(cat));
            }
        }
        return j;
    }

    @Inject(method = "tickChunks", at = @At("RETURN"))
    private void onFinishSpawnWorldCycle(CallbackInfo ci) {
        IWorldInfo levelProperties_1 = this.level.getLevelData(); // levelProperies class
        boolean boolean_3 = levelProperties_1.getGameTime() % 400L == 0L;
        if (SpawnReporter.track_spawns > 0L && SpawnReporter.local_spawns != null) {
            for (EntityClassification cat : EntityClassification.values()) {
                RegistryKey<World> dim = level.dimension(); // getDimensionType;
                Pair<RegistryKey<World>, EntityClassification> key = Pair.of(dim, cat);
                int spawnTries = SpawnReporter.spawn_tries.get(cat);
                if (!SpawnReporter.local_spawns.containsKey(cat)) {
                    if (!cat.isPersistent() || boolean_3) // isAnimal
                    {
                        // fill mobcaps for that category so spawn got cancelled
                        SpawnReporter.spawn_ticks_full.put(key,
                                SpawnReporter.spawn_ticks_full.get(key) + spawnTries);
                    }

                } else if (SpawnReporter.local_spawns.get(cat) > 0) {
                    // tick spawned mobs for that type
                    SpawnReporter.spawn_ticks_succ.put(key,
                            SpawnReporter.spawn_ticks_succ.get(key) + spawnTries);
                    SpawnReporter.spawn_ticks_spawns.put(key,
                            SpawnReporter.spawn_ticks_spawns.get(key) +
                                    SpawnReporter.local_spawns.get(cat));
                    // this will be off comparing to 1.13 as that would succeed if
                    // ANY tries in that round were successful.
                    // there will be much more difficult to mix in
                    // considering spawn tries to remove, as with warp
                    // there is little need for them anyways.
                } else // spawn no mobs despite trying
                {
                    //tick didn's spawn mobs of that type
                    SpawnReporter.spawn_ticks_fail.put(key,
                            SpawnReporter.spawn_ticks_fail.get(key) + spawnTries);
                }
            }
        }
        SpawnReporter.local_spawns = null;
    }
}
