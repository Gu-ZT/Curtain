package dev.dubhe.curtain.mixins;

import dev.dubhe.curtain.utils.SpawnReporter;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static dev.dubhe.curtain.utils.SpawnReporter.MAGIC_NUMBER;
import static net.minecraft.world.level.NaturalSpawner.SPAWNING_CATEGORIES;

@Mixin(NaturalSpawner.class)
public abstract class NaturalSpawnerMixin {
    @Redirect(method = "spawnCategoryForPosition(Lnet/minecraft/world/entity/MobCategory;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/chunk/ChunkAccess;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/NaturalSpawner$SpawnPredicate;Lnet/minecraft/world/level/NaturalSpawner$AfterSpawnCallback;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;addFreshEntityWithPassengers(Lnet/minecraft/world/entity/Entity;)V"
            )
    )
    private static void spawnEntity(
            ServerLevel world,
            Entity entity_1,
            MobCategory group,
            ServerLevel world2,
            ChunkAccess chunk,
            BlockPos pos,
            NaturalSpawner.SpawnPredicate checker,
            NaturalSpawner.AfterSpawnCallback runner
    ) {
        if (SpawnReporter.track_spawns > 0L && SpawnReporter.local_spawns != null) {
            SpawnReporter.registerSpawn((Mob) entity_1, group, entity_1.blockPosition());
        }
        if (!SpawnReporter.mock_spawns) {
            world.addFreshEntityWithPassengers(entity_1);
        }
    }
    // TODO：Mixin 失效，不会修
//    @Redirect(
//            method = "spawnCategoryForPosition(Lnet/minecraft/world/entity/MobCategory;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/chunk/ChunkAccess;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/NaturalSpawner$SpawnPredicate;Lnet/minecraft/world/level/NaturalSpawner$AfterSpawnCallback;)V",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/world/entity/Mob;finalizeSpawn(Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/world/DifficultyInstance;Lnet/minecraft/world/entity/MobSpawnType;Lnet/minecraft/world/entity/SpawnGroupData;Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/world/entity/SpawnGroupData;"
//            )
//    )
//    private static SpawnGroupData spawnEntity(Mob mobEntity, ServerLevelAccessor serverWorldAccess, DifficultyInstance difficulty, MobSpawnType spawnReason, SpawnGroupData entityData, CompoundTag entityTag) {
//        if (!SpawnReporter.mock_spawns) // WorldAccess
//            return mobEntity.finalizeSpawn(serverWorldAccess, difficulty, spawnReason, entityData, entityTag);
//        return null;
//    }

    @Redirect(method = "spawnForChunk",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/NaturalSpawner;spawnCategoryForChunk(Lnet/minecraft/world/entity/MobCategory;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/chunk/LevelChunk;Lnet/minecraft/world/level/NaturalSpawner$SpawnPredicate;Lnet/minecraft/world/level/NaturalSpawner$AfterSpawnCallback;)V"
            )
    )
    // inject our repeat of spawns if more spawn ticks per tick are chosen.
    private static void spawnMultipleTimes(
            MobCategory category,
            ServerLevel world,
            LevelChunk chunk,
            NaturalSpawner.SpawnPredicate checker,
            NaturalSpawner.AfterSpawnCallback runner
    ) {
        for (int i = 0; i < SpawnReporter.spawn_tries.get(category); i++) {
            NaturalSpawner.spawnCategoryForChunk(category, world, chunk, checker, runner);
        }
    }

    @Inject(method = "spawnForChunk", at = @At("HEAD"))
    // allows to change mobcaps and captures each category try per dimension before it fails due to full mobcaps.
    private static void checkSpawns(
            ServerLevel world,
            LevelChunk chunk,
            NaturalSpawner.SpawnState info,
            boolean spawnAnimals,
            boolean spawnMonsters,
            boolean shouldSpawnAnimals,
            CallbackInfo ci
    ) {
        if (SpawnReporter.track_spawns > 0L) {
            MobCategory[] var6 = SPAWNING_CATEGORIES;
            int var7 = var6.length;

            for (int var8 = 0; var8 < var7; ++var8) {
                MobCategory entityCategory = var6[var8];
                if ((spawnAnimals || !entityCategory.isFriendly()) && (spawnMonsters || entityCategory.isFriendly()) && (shouldSpawnAnimals || !entityCategory.isPersistent())) {
                    ResourceKey<Level> dim = world.dimension(); // getDimensionType;
                    int newCap = entityCategory.getMaxInstancesPerChunk();  //(int) ((double)entityCategory.getCapacity()*(Math.pow(2.0,(SpawnReporter.mobcap_exponent/4))));
                    int int_2 = SpawnReporter.chunkCounts.get(dim); // eligible chunks for spawning
                    int int_3 = newCap * int_2 / MAGIC_NUMBER; //current spawning limits
                    int mobCount = info.getMobCategoryCounts().getInt(entityCategory);

                    if (SpawnReporter.track_spawns > 0L && !SpawnReporter.first_chunk_marker.contains(entityCategory)) {
                        SpawnReporter.first_chunk_marker.add(entityCategory);
                        //first chunk with spawn eligibility for that category
                        Pair<ResourceKey<Level>, MobCategory> key = Pair.of(dim, entityCategory);


                        int spawnTries = SpawnReporter.spawn_tries.get(entityCategory);

                        SpawnReporter.spawn_attempts.put(key,
                                SpawnReporter.spawn_attempts.get(key) + spawnTries);

                        SpawnReporter.spawn_cap_count.put(key,
                                SpawnReporter.spawn_cap_count.get(key) + mobCount);
                    }

                    if (mobCount <= int_3 || SpawnReporter.mock_spawns) //TODO this will not float with player based mobcaps
                    {
                        //place 0 to indicate there were spawn attempts for a category
                        //if (entityCategory != EntityCategory.CREATURE || world.getServer().getTicks() % 400 == 0)
                        // this will only be called once every 400 ticks anyways
                        SpawnReporter.local_spawns.putIfAbsent(entityCategory, 0L);

                        //else
                        //full mobcaps - and key in local_spawns will be missing
                    }
                }
            }
        }
    }
}
