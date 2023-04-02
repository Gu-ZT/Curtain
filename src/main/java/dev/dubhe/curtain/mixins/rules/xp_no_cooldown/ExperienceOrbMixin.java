package dev.dubhe.curtain.mixins.rules.xp_no_cooldown;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.world.entity.ExperienceOrb;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(ExperienceOrb.class)
public abstract class ExperienceOrbMixin {
    @ModifyConstant(
            method = "playerTouch",
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraftforge/eventbus/api/IEventBus;post(Lnet/minecraftforge/eventbus/api/Event;)Z"
                    ),
                    to = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/world/entity/ExperienceOrb;repairPlayerItems(Lnet/minecraft/world/entity/player/Player;I)I"
                    )
            ),
            constant = @Constant(intValue = 2),
            remap = false
    )
    private int ModifyPlayerTakeXpDelay(int delay) {
        if (CurtainRules.xpNoCooldown)
            return 0;
        else
            return delay;
    }

}
