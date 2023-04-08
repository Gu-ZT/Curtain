package dev.dubhe.curtain.mixins;

import dev.dubhe.curtain.CurtainRules;
import dev.dubhe.curtain.features.logging.helper.TNTLogHelper;
import dev.dubhe.curtain.features.rules.fakes.TntEntityInterface;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TNTEntity.class)
public abstract class PrimedTntMixin extends Entity implements TntEntityInterface {

    @Shadow
    public abstract int getFuse();

    private TNTLogHelper logHelper;
    private boolean mergeBool = false;
    private int mergedTNT = 1;

    public PrimedTntMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/entity/LivingEntity;)V", at = @At("RETURN"))
    private void modifyTNTAngle(World world, double x, double y, double z, LivingEntity entity, CallbackInfo ci) {
        if (CurtainRules.hardcodeTNTAngle != -1.0D) {
            setDeltaMovement(-Math.sin(CurtainRules.hardcodeTNTAngle) * 0.02, 0.2, -Math.cos(CurtainRules.hardcodeTNTAngle) * 0.02);
        }
    }

    @Inject(method = "<init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/World;)V", at = @At("RETURN"))
    private void initTNTLoggerPrime(EntityType<? extends TNTEntity> type, World world, CallbackInfo ci) {
        if (!world.isClientSide) {
            logHelper = new TNTLogHelper();
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void initTracker(CallbackInfo ci) {
        if (logHelper != null && !logHelper.initialized) {
            logHelper.onPrimed(getX(), getY(), getZ(), getDeltaMovement());
        }
    }

    @Inject(method = "<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/entity/LivingEntity;)V", at = @At(value = "RETURN"))
    private void initTNTLogger(World world, double x, double y, double z, LivingEntity entity, CallbackInfo ci) {
        if (CurtainRules.tntPrimerMomentumRemoved) {
            this.setDeltaMovement(0, 0.20000000298023224D, 0);
        }
    }

    @Inject(method = "explode", at = @At(value = "HEAD"))
    private void onExplode(CallbackInfo ci) {
        if (logHelper != null) {
            logHelper.onExploded(getX(), getY(), getZ(), this.level.getGameTime());
        }
        if (mergedTNT > 1) {
            for (int i = 0; i < mergedTNT - 1; i++) {
                this.level.explode(this, this.getX(), this.getY() + (double) (this.getBbHeight() / 16.0F),
                        this.getZ(),
                        4.0F,
                        Explosion.Mode.DESTROY);
            }
        }
    }

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/item/TNTEntity;setDeltaMovement(Lnet/minecraft/util/math/vector/Vector3d;)V",
                    ordinal = 2
            )
    )
    private void tryMergeTnt(CallbackInfo ci) {
        // Merge code for combining tnt into a single entity if they happen to exist in the same spot, same fuse, no motion CARPET-XCOM
        if (CurtainRules.mergeTNT) {
            Vector3d velocity = getDeltaMovement();
            if (!level.isClientSide && mergeBool && velocity.x == 0 && velocity.y == 0 && velocity.z == 0) {
                mergeBool = false;
                for (Entity entity : level.getEntities(this, this.getBoundingBox())) {
                    if (entity instanceof TNTEntity && !entity.removed) {
                        TNTEntity entityTNTPrimed = (TNTEntity) entity;
                        Vector3d tntVelocity = entityTNTPrimed.getDeltaMovement();
                        if (tntVelocity.x == 0 && tntVelocity.y == 0 && tntVelocity.z == 0
                                && this.getX() == entityTNTPrimed.getX() && this.getZ() == entityTNTPrimed.getZ() && this.getY() == entityTNTPrimed.getY()
                                && getFuse() == entityTNTPrimed.getFuse()) {
                            mergedTNT += ((TntEntityInterface) entityTNTPrimed).getMergedTNT();
                            entityTNTPrimed.remove(); // discard remove();
                        }
                    }
                }
            }
        }
    }

    @Inject(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/item/TNTEntity;life:I"))
    private void setMergeable(CallbackInfo ci) {
        // Merge code, merge only tnt that have had a chance to move CARPET-XCOM
        Vector3d velocity = getDeltaMovement();
        if (!level.isClientSide && (velocity.y != 0 || velocity.x != 0 || velocity.z != 0)) {
            mergeBool = true;
        }
    }

    @Override
    public int getMergedTNT() {
        return mergedTNT;
    }
}
