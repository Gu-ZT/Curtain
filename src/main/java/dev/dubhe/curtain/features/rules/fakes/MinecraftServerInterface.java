package dev.dubhe.curtain.features.rules.fakes;

import dev.dubhe.curtain.utils.ServerTickRateManager;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelStorageSource;

import java.util.Map;
import java.util.function.BooleanSupplier;

public interface MinecraftServerInterface
{
    void forceTick(BooleanSupplier sup);
    LevelStorageSource.LevelStorageAccess getCMSession();
    Map<ResourceKey<Level>, ServerLevel> getCMWorlds();
    void reloadAfterReload(RegistryAccess newRegs);

    MinecraftServer.ReloadableResources getResourceManager();


    ServerTickRateManager getTickRateManager();
}
