package dev.dubhe.curtain.mixins;

import dev.dubhe.curtain.utils.SpawnReporter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.WorldEntitySpawner;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static dev.dubhe.curtain.utils.SpawnReporter.MAGIC_NUMBER;
import static net.minecraft.world.spawner.WorldEntitySpawner.SPAWNING_CATEGORIES;

@Mixin(WorldEntitySpawner.class)
public abstract class NaturalSpawnerMixin {
    @Redirect(method = "spawnCategoryForPosition(Lnet/minecraft/entity/EntityClassification;Lnet/minecraft/world/server/ServerWorld;Lnet/minecraft/world/chunk/IChunk;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/spawner/WorldEntitySpawner$IDensityCheck;Lnet/minecraft/world/spawner/WorldEntitySpawner$IOnSpawnDensityAdder;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/server/ServerWorld;addFreshEntityWithPassengers(Lnet/minecraft/entity/Entity;)V"
            )
    )
    private static void spawnEntity(
            ServerWorld world,
            Entity entity_1,
            EntityClassification group,
            ServerWorld world2,
            IChunk chunk,
            BlockPos pos,
            WorldEntitySpawner.IDensityCheck checker,
            WorldEntitySpawner.IOnSpawnDensityAdder runner
    ) {
        if (SpawnReporter.track_spawns > 0L && SpawnReporter.local_spawns != null) {
            SpawnReporter.registerSpawn((MobEntity) entity_1, group, entity_1.blockPosition());
        }
        if (!SpawnReporter.mock_spawns) {
            world.addFreshEntityWithPassengers(entity_1);
        }
    }

    @Redirect(method = "spawnCategoryForPosition(Lnet/minecraft/entity/EntityClassification;Lnet/minecraft/world/server/ServerWorld;Lnet/minecraft/world/chunk/IChunk;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/spawner/WorldEntitySpawner$IDensityCheck;Lnet/minecraft/world/spawner/WorldEntitySpawner$IOnSpawnDensityAdder;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/MobEntity;finalizeSpawn(Lnet/minecraft/world/IServerWorld;Lnet/minecraft/world/DifficultyInstance;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/entity/ILivingEntityData;Lnet/minecraft/nbt/CompoundNBT;)Lnet/minecraft/entity/ILivingEntityData;"
            )
    )
    private static ILivingEntityData spawnEntity(
            MobEntity mobEntity, IServerWorld serverWorldAccess, DifficultyInstance difficulty, SpawnReason spawnReason, ILivingEntityData entityData, CompoundNBT entityTag
    ) {
        if (!SpawnReporter.mock_spawns) {
            return mobEntity.finalizeSpawn(serverWorldAccess, difficulty, spawnReason, entityData, entityTag);
        }
        return null;
    }

    @Redirect(method = "spawnForChunk",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/spawner/WorldEntitySpawner;spawnCategoryForChunk(Lnet/minecraft/entity/EntityClassification;Lnet/minecraft/world/server/ServerWorld;Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/world/spawner/WorldEntitySpawner$IDensityCheck;Lnet/minecraft/world/spawner/WorldEntitySpawner$IOnSpawnDensityAdder;)V"
            )
    )
    // inject our repeat of spawns if more spawn ticks per tick are chosen.
    private static void spawnMultipleTimes(
            EntityClassification category, ServerWorld world, Chunk chunk, WorldEntitySpawner.IDensityCheck checker, WorldEntitySpawner.IOnSpawnDensityAdder runner
    ) {
        for (int i = 0; i < SpawnReporter.spawn_tries.get(category); i++) {
            WorldEntitySpawner.spawnCategoryForChunk(category, world, chunk, checker, runner);
        }
    }

    @Inject(method = "spawnForChunk", at = @At("HEAD"))
    // allows to change mobcaps and captures each category try per dimension before it fails due to full mobcaps.
    private static void checkSpawns(
            ServerWorld world, Chunk chunk, WorldEntitySpawner.EntityDensityManager info, boolean spawnAnimals, boolean spawnMonsters, boolean shouldSpawnAnimals, CallbackInfo ci
    ) {
        if (SpawnReporter.track_spawns > 0L) {
            EntityClassification[] var6 = SPAWNING_CATEGORIES;
            int var7 = var6.length;

            for (int var8 = 0; var8 < var7; ++var8) {
                EntityClassification entityCategory = var6[var8];
                if ((spawnAnimals || !entityCategory.isFriendly()) && (spawnMonsters || entityCategory.isFriendly()) && (shouldSpawnAnimals || !entityCategory.isPersistent())) {
                    RegistryKey<World> dim = world.dimension(); // getDimensionType;
                    int newCap = entityCategory.getMaxInstancesPerChunk();  //(int) ((double)entityCategory.getCapacity()*(Math.pow(2.0,(SpawnReporter.mobcap_exponent/4))));
                    int int_2 = SpawnReporter.chunkCounts.get(dim); // eligible chunks for spawning
                    int int_3 = newCap * int_2 / MAGIC_NUMBER; //current spawning limits
                    int mobCount = info.getMobCategoryCounts().getInt(entityCategory);

                    if (SpawnReporter.track_spawns > 0L && !SpawnReporter.first_chunk_marker.contains(entityCategory)) {
                        SpawnReporter.first_chunk_marker.add(entityCategory);
                        //first chunk with spawn eligibility for that category
                        Pair<RegistryKey<World>, EntityClassification> key = Pair.of(dim, entityCategory);


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
