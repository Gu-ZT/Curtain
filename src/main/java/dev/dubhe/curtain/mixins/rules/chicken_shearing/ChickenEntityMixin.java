package dev.dubhe.curtain.mixins.rules.chicken_shearing;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import javax.annotation.Nonnull;

@Mixin(ChickenEntity.class)
public abstract class ChickenEntityMixin extends AnimalEntity {
    protected ChickenEntityMixin(EntityType<? extends AnimalEntity> pEntityType, World pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public @Nonnull ActionResultType interactAt(@Nonnull PlayerEntity pPlayer, @Nonnull Vector3d pVec, @Nonnull Hand pHand) {
        ItemStack stack = pPlayer.getItemInHand(pHand);
        if (CurtainRules.chickenShearing && stack.getItem() == Items.SHEARS && !this.isBaby()) {
            boolean tookDamage = this.hurt(DamageSource.GENERIC, 1);
            if (tookDamage) {
                this.spawnAtLocation(Items.FEATHER, 1);
                stack.hurtAndBreak(1, (LivingEntity) pPlayer, ((entity) -> pPlayer.broadcastBreakEvent(pHand)));
                return ActionResultType.SUCCESS;
            }
        }
        return super.interactAt(pPlayer, pVec, pHand);
    }
}
