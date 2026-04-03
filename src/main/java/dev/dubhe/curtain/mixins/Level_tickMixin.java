package dev.dubhe.curtain.mixins;

import dev.dubhe.curtain.features.rules.fakes.LevelInterface;
import dev.dubhe.curtain.utils.CurtainProfiler;
import dev.dubhe.curtain.utils.TickRateManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Mixin(Level.class)
public abstract class Level_tickMixin implements LevelInterface
{
    @Shadow @Final public boolean isClientSide;
    CurtainProfiler.ProfilerToken currentSection;
    CurtainProfiler.ProfilerToken entitySection;

    Map<EntityType<?>, Entity> precookedMobs = new HashMap<>();

    @Override
    public Map<EntityType<?>, Entity> getPrecookedMobs()
    {
        return precookedMobs;
    }

    @Inject(method = "tickBlockEntities", at = @At("HEAD"))
    private void startBlockEntities(CallbackInfo ci) {
        currentSection = CurtainProfiler.start_section((Level) (Object) this, "Block Entities", CurtainProfiler.TYPE.GENERAL);
    }

    @Inject(method = "tickBlockEntities", at = @At("TAIL"))
    private void endBlockEntities(CallbackInfo ci) {
        CurtainProfiler.end_current_section(currentSection);
    }

    @Inject(method = "guardEntityTick", at = @At("HEAD"), cancellable = true)
    private void startEntity(Consumer<Entity> consumer_1, Entity e, CallbackInfo ci)
    {
        TickRateManager trm = tickRateManager();
        if (!trm.shouldEntityTick(e))
        {
            ci.cancel();
        }

        entitySection =  CurtainProfiler.start_entity_section((Level) (Object) this, e, CurtainProfiler.TYPE.ENTITY);
    }

    @Inject(method = "guardEntityTick", at = @At("TAIL"))
    private void endEntity(Consumer<Entity> call, Entity e, CallbackInfo ci) {
        CurtainProfiler.end_current_entity_section(entitySection);
    }


}
