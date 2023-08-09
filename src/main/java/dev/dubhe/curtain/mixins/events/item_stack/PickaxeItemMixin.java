package dev.dubhe.curtain.mixins.events.item_stack;

import dev.dubhe.curtain.events.events.ItemStackEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PickaxeItem.class)
public abstract class PickaxeItemMixin extends DiggerItem {
    private PickaxeItemMixin(float attackDamage, float attackSpeed, Tier material, TagKey<Block> tag, Properties settings) {
        super(attackDamage, attackSpeed, material, tag, settings);
    }

    @Override
    public float getDestroySpeed(@NotNull ItemStack stack, @NotNull BlockState state) {
        ItemStackEvent.BreakSpeed event = new ItemStackEvent.BreakSpeed(stack, state, super.getDestroySpeed(stack, state), this.speed);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getSpeed();
    }
}
