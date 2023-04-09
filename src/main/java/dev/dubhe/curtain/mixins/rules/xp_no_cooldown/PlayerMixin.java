package dev.dubhe.curtain.mixins.rules.xp_no_cooldown;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerEntity.class)
public abstract class PlayerMixin {
//    @Shadow
//    protected abstract void touch(Entity entity);
//
//    @Redirect(method = "aiStep",at = @At(value = "INVOKE",target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
//    public boolean processXpOrbCollisions(List<Entity> instance, Object e) {
//        Entity entity = (Entity) e;
//        if (CurtainRules.xpNoCooldown) {
//            this.touch(entity);
//            return true;
//        }
//        return instance.add(entity);
//    }

}
