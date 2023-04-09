package dev.dubhe.curtain.mixins.rules.xp_no_cooldown;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(Player.class)
public abstract class PlayerMixin {
    @Shadow
    protected abstract void touch(Entity entity);

    @Redirect(method = "aiStep", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    public boolean processXpOrbCollisions(List<Entity> instance, Object e) {
        Entity entity = (Entity) e;
        if (CurtainRules.xpNoCooldown) {
            this.touch(entity);
            return true;
        }
        return instance.add(entity);
    }

}
