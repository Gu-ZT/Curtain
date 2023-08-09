package dev.dubhe.curtain.mixins.events.item_stack;

import dev.dubhe.curtain.events.events.ItemStackEvent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Inject(method = "use", at = @At("HEAD"))
    private void use(World pLevel, PlayerEntity pPlayer, Hand pUsedHand, CallbackInfoReturnable<ActionResult<ItemStack>> cir) {
        MinecraftForge.EVENT_BUS.post(new ItemStackEvent.Use((ItemStack) (Object) this, pLevel, pPlayer, pUsedHand));
    }

    @Inject(method = "hurtAndBreak", at = @At("HEAD"))
    private <T extends LivingEntity> void hurtAndBreak(int amount, T entity, Consumer<T> onBroken, CallbackInfo ci) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            MinecraftForge.EVENT_BUS.post(new ItemStackEvent.HurtAndBreak((ItemStack) (Object) this, amount, player));
        }
    }
}
