package dev.dubhe.curtain.mixins.events.item_stack;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolItem;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Set;

@Mixin(PickaxeItem.class)
public abstract class PickaxeItemMixin extends ToolItem {
    public PickaxeItemMixin(float attackDamage, float attackSpeed, IItemTier material, Set<Block> tag, Properties settings) {
        super(attackDamage, attackSpeed, material, tag, settings);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        Material material = state.getMaterial();
        if (CurtainRules.missingTools && material == Material.GLASS) return speed;
        return super.getDestroySpeed(stack, state);
    }
}
