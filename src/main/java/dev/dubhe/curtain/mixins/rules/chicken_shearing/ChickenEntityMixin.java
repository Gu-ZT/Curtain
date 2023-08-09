package dev.dubhe.curtain.mixins.rules.chicken_shearing;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Chicken.class)
public abstract class ChickenEntityMixin extends Animal {
    protected ChickenEntityMixin(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public @NotNull InteractionResult interactAt(@NotNull Player pPlayer, @NotNull Vec3 pVec, @NotNull InteractionHand pHand) {
        ItemStack stack = pPlayer.getItemInHand(pHand);
        if (CurtainRules.chickenShearing && stack.getItem() == Items.SHEARS && !this.isBaby()) {
            boolean tookDamage = this.hurt(pPlayer.level().damageSources().generic(), 1);
            if (tookDamage) {
                this.spawnAtLocation(Items.FEATHER, 1);
                stack.hurtAndBreak(1, (LivingEntity) pPlayer, ((entity) -> pPlayer.broadcastBreakEvent(pHand)));
                return InteractionResult.SUCCESS;
            }
        }
        return super.interactAt(pPlayer, pVec, pHand);
    }
}
