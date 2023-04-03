package dev.dubhe.curtain.mixins.rules.super_lead;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.npc.Npc;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractVillager.class)
public abstract class AbstractVillagerMixin extends AgeableMob implements InventoryCarrier, Npc, Merchant {
    protected AbstractVillagerMixin(EntityType<? extends AgeableMob> type, Level level) {
        super(type, level);
    }

    @Inject(method = "canBeLeashed", at = @At("RETURN"), cancellable = true)
    private void canBeLeashed(Player player, CallbackInfoReturnable<Boolean> cir) {
        if (CurtainRules.superLead) {
            cir.setReturnValue(!this.isLeashed());
        }
    }
}
