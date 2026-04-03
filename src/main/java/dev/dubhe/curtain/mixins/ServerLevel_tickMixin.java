package dev.dubhe.curtain.mixins;

import dev.dubhe.curtain.features.rules.fakes.LevelInterface;
import dev.dubhe.curtain.features.rules.fakes.MinecraftServerInterface;
import dev.dubhe.curtain.utils.CurtainProfiler;
import dev.dubhe.curtain.utils.TickRateManager;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.raid.Raids;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Mixin(ServerLevel.class)
public abstract class ServerLevel_tickMixin extends Level implements LevelInterface
{
    protected ServerLevel_tickMixin(final WritableLevelData writableLevelData, final ResourceKey<Level> resourceKey, final Holder<DimensionType> holder, final Supplier<ProfilerFiller> supplier, final boolean bl, final boolean bl2, final long l, final int i)
    {
        super(writableLevelData, resourceKey, holder, supplier, bl, bl2, l);
    }

    @Override
    public TickRateManager tickRateManager()
    {
        return ((MinecraftServerInterface)getServer()).getTickRateManager();
    }

    @Shadow protected abstract void runBlockEvents();

    @Shadow protected abstract void tickTime();

    private CurtainProfiler.ProfilerToken currentSection;

    @Inject(method = "tick", at = @At(
            value = "CONSTANT",
            args = "stringValue=weather"
    ))
    private void startWeatherSection(BooleanSupplier booleanSupplier_1, CallbackInfo ci)
    {
        currentSection = CurtainProfiler.start_section((Level)(Object)this, "Environment", CurtainProfiler.TYPE.GENERAL);
    }
    @Inject(method = "tick", at = @At(
            value = "CONSTANT",
            args = "stringValue=tickPending"
    ))
    private void stopWeatherStartTileTicks(BooleanSupplier booleanSupplier_1, CallbackInfo ci)
    {
        if (currentSection != null)
        {
            CurtainProfiler.end_current_section(currentSection);
            currentSection = CurtainProfiler.start_section((Level) (Object) this, "Schedule Ticks", CurtainProfiler.TYPE.GENERAL);
        }
    }
    @Inject(method = "tick", at = @At(
            value = "CONSTANT",
            args = "stringValue=raid"
    ))
    private void stopTileTicksStartRaid(BooleanSupplier booleanSupplier_1, CallbackInfo ci)
    {
        if (currentSection != null)
        {
            CurtainProfiler.end_current_section(currentSection);
            currentSection = CurtainProfiler.start_section((Level) (Object) this, "Raid", CurtainProfiler.TYPE.GENERAL);
        }
    }

    @Inject(method = "tick", at = @At(
            value = "CONSTANT",
            args = "stringValue=chunkSource"
    ))
    private void stopRaid(BooleanSupplier booleanSupplier_1, CallbackInfo ci)
    {
        if (currentSection != null)
        {
            CurtainProfiler.end_current_section(currentSection);
        }
    }
    @Inject(method = "tick", at = @At(
            value = "CONSTANT",
            args = "stringValue=blockEvents"
    ))
    private void startBlockEvents(BooleanSupplier booleanSupplier_1, CallbackInfo ci)
    {
        currentSection = CurtainProfiler.start_section((Level) (Object) this, "Block Events", CurtainProfiler.TYPE.GENERAL);
    }

    @Inject(method = "tick", at = @At(
            value = "CONSTANT",
            args = "stringValue=entities"
    ))
    private void stopBlockEventsStartEntitySection(BooleanSupplier booleanSupplier_1, CallbackInfo ci)
    {
        if (currentSection != null)
        {
            CurtainProfiler.end_current_section(currentSection);
            currentSection = CurtainProfiler.start_section((Level) (Object) this, "Entities", CurtainProfiler.TYPE.GENERAL);
        }
    }

    @Inject(method = "tick", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerLevel;tickBlockEntities()V",
            shift = At.Shift.BEFORE
    ))
    private void endEntitySection(BooleanSupplier booleanSupplier_1, CallbackInfo ci)
    {
        CurtainProfiler.end_current_section(currentSection);
        currentSection = null;
    }

    // Chunk

    @Inject(method = "tickChunk", at = @At("HEAD"))
    private void startThunderSpawningSection(CallbackInfo ci) {
        // Counting it in spawning because it's spawning skeleton horses
        currentSection = CurtainProfiler.start_section((Level) (Object) this, "Spawning", CurtainProfiler.TYPE.GENERAL);
    }

    @Inject(method = "tickChunk", at = @At(
            value = "CONSTANT",
            args = "stringValue=iceandsnow"
    ))
    private void endThunderSpawningAndStartIceSnowRandomTicks(CallbackInfo ci) {
        if (currentSection != null) {
            CurtainProfiler.end_current_section(currentSection);
            currentSection = CurtainProfiler.start_section((Level) (Object) this, "Environment", CurtainProfiler.TYPE.GENERAL);
        }
    }

    @Inject(method = "tickChunk", at = @At(
            value = "CONSTANT",
            args = "stringValue=tickBlocks"
    ))
    private void endIceAndSnowAndStartRandomTicks(CallbackInfo ci) {
        if (currentSection != null) {
            CurtainProfiler.end_current_section(currentSection);
            currentSection = CurtainProfiler.start_section((Level) (Object) this, "Random Ticks", CurtainProfiler.TYPE.GENERAL);
        }
    }

    @Inject(method = "tickChunk", at = @At("RETURN"))
    private void endRandomTicks(CallbackInfo ci) {
        if (currentSection != null) {
            CurtainProfiler.end_current_section(currentSection);
            currentSection = null;
        }
    }

    //// freeze

    @Redirect(method = "tick", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/border/WorldBorder;tick()V"
    ))
    private void tickWorldBorder(WorldBorder worldBorder)
    {
        if (tickRateManager().runsNormally()) worldBorder.tick();
    }

    @Inject(method = "advanceWeatherCycle", cancellable = true, at = @At("HEAD"))
    private void tickWeather(CallbackInfo ci)
    {
        if (!tickRateManager().runsNormally()) ci.cancel();
    }

    @Redirect(method = "tick", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerLevel;tickTime()V"
    ))
    private void tickTimeConditionally(ServerLevel serverWorld)
    {
        if (tickRateManager().runsNormally()) tickTime();
    }

    @Redirect(method = "tick", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerLevel;isDebug()Z"
    ))
    private boolean tickPendingBlocks(ServerLevel serverWorld)
    {
        if (!tickRateManager().runsNormally()) return true;
        return serverWorld.isDebug(); // isDebug()
    }

    @Redirect(method = "tick", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/raid/Raids;tick()V"
    ))
    private void tickConditionally(Raids raidManager)
    {
        if (tickRateManager().runsNormally()) raidManager.tick();
    }

    @Redirect(method = "tick", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerLevel;runBlockEvents()V"
    ))
    private void tickConditionally(ServerLevel serverWorld)
    {
        if (tickRateManager().runsNormally()) runBlockEvents();
    }
}
