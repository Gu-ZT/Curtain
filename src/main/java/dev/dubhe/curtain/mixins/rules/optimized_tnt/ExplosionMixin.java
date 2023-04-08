package dev.dubhe.curtain.mixins.rules.optimized_tnt;

import dev.dubhe.curtain.CurtainRules;
import dev.dubhe.curtain.features.logging.helper.ExplosionLogHelper;
import dev.dubhe.curtain.utils.OptimizedExplosion;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.ExplosionContext;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Explosion.class)
public class ExplosionMixin {
    @Shadow
    @Final
    private World level;
    @Shadow @Final private List<BlockPos> toBlow;
    private ExplosionLogHelper eLogger;

    @Inject(method = "explode", at = @At("HEAD"), cancellable = true)
    private void onExplosionA(CallbackInfo ci) {
        if (CurtainRules.optimizedTNT) {
            OptimizedExplosion.doExplosionA((Explosion) (Object) this, eLogger);
            ci.cancel();
        }
    }

    @Inject(method = "finalizeExplosion", at = @At("HEAD"), cancellable = true)
    private void onExplosionB(boolean spawnParticles, CallbackInfo ci) {
        if (eLogger != null) {
            eLogger.setAffectBlocks(!toBlow.isEmpty());
            eLogger.onExplosionDone(this.level.getGameTime());
        }
        if (CurtainRules.explosionNoBlockDamage) {
            toBlow.clear();
        }
        if (CurtainRules.optimizedTNT) {
            OptimizedExplosion.doExplosionB((Explosion) (Object) this, spawnParticles);
            ci.cancel();
        }
    }

    @Redirect(method = "explode", require = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"))
    private BlockState noBlockCalcsWithNoBLockDamage(World world, BlockPos pos) {
        if (CurtainRules.explosionNoBlockDamage) {
            return Blocks.BEDROCK.defaultBlockState();
        }
        return world.getBlockState(pos);
    }

    @Inject(method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/DamageSource;Lnet/minecraft/world/ExplosionContext;DDDFZLnet/minecraft/world/Explosion$Mode;)V", at = @At(value = "RETURN"))
    private void onExplosionCreated(World world, Entity entity, DamageSource damageSource, ExplosionContext explosionBehavior, double x, double y, double z, float power, boolean createFire, Explosion.Mode destructionType, CallbackInfo ci) {
        if (!world.isClientSide) {
            eLogger = new ExplosionLogHelper(x, y, z, power, createFire, destructionType, level.registryAccess());
        }
    }

    @Redirect(method = "explode", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setDeltaMovement(Lnet/minecraft/util/math/vector/Vector3d;)V"))
    private void setVelocityAndUpdateLogging(Entity entity, Vector3d velocity) {
        if (eLogger != null) {
            eLogger.onEntityImpacted(entity, velocity.subtract(entity.getDeltaMovement()));
        }
        entity.setDeltaMovement(velocity);
    }
}
