package dev.dubhe.curtain.mixins.rules.stackable_shulker_boxes;

import dev.dubhe.curtain.CurtainRules;
import dev.dubhe.curtain.features.rules.fakes.ItemEntityInterface;
import dev.dubhe.curtain.utils.InventoryHelper;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity implements ItemEntityInterface {
    @Shadow
    private int pickupDelay;

    @Shadow
    private int age;

    public ItemEntityMixin(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public int getPickupDelayCM() {
        return this.pickupDelay;
    }

    @Inject(method = "<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;)V", at = @At("RETURN"))
    private void removeEmptyShulkerBoxTags(World worldIn, double x, double y, double z, ItemStack stack, CallbackInfo ci) {
        if (CurtainRules.shulkerBoxStackSize > 1
                && stack.getItem() instanceof BlockItem blockItem
                && blockItem.getBlock() instanceof ShulkerBoxBlock) {
            InventoryHelper.cleanUpShulkerBoxTag(stack);
        }
    }

    @Redirect(
            method = "isMergable()Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;getMaxStackSize()I"
            )
    )
    private int getItemStackMaxAmount(ItemStack stack) {
        if (CurtainRules.shulkerBoxStackSize > 1
                && stack.getItem() instanceof BlockItem blockItem
                && blockItem.getBlock() instanceof ShulkerBoxBlock) {
            return CurtainRules.shulkerBoxStackSize;
        }
        return stack.getMaxStackSize();
    }

    @Inject(
            method = "tryToMerge",
            at = @At("HEAD"),
            cancellable = true
    )
    private void tryStackShulkerBoxes(ItemEntity other, CallbackInfo ci) {
        ItemEntity self = (ItemEntity) (Object) this;
        ItemStack selfStack = self.getItem();
        if (CurtainRules.shulkerBoxStackSize == 1
                || !(selfStack.getItem() instanceof BlockItem blockItem)
                || !(blockItem.getBlock() instanceof ShulkerBoxBlock)) {
            return;
        }

        ItemStack otherStack = other.getItem();
        if (selfStack.getItem() == otherStack.getItem()
                && !InventoryHelper.shulkerBoxHasItems(selfStack)
                && !InventoryHelper.shulkerBoxHasItems(otherStack)
                && Objects.equals(selfStack.getTag(), otherStack.getTag())
                && selfStack.getCount() != CurtainRules.shulkerBoxStackSize) {
            int amount = Math.min(otherStack.getCount(), CurtainRules.shulkerBoxStackSize - selfStack.getCount());

            selfStack.grow(amount);
            self.setItem(selfStack);

            this.pickupDelay = Math.max(((ItemEntityInterface) other).getPickupDelayCM(), this.pickupDelay);
            this.age = Math.min(other.getAge(), this.age);

            otherStack.shrink(amount);
            if (otherStack.isEmpty()) {
                other.remove();
            } else {
                other.setItem(otherStack);
            }
            ci.cancel();
        }
    }
}
