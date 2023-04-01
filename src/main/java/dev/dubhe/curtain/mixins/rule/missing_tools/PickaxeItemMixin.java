package dev.dubhe.curtain.mixins.rule.missing_tools;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PickaxeItem.class)
public class PickaxeItemMixin extends DiggerItem {
    protected PickaxeItemMixin(float attackDamage, float attackSpeed, Tier material, TagKey<Block> tag, Item.Properties settings) {
        super(attackDamage, attackSpeed, material, tag, settings);
    }

    @Override
    public float getDestroySpeed(@NotNull ItemStack stack, @NotNull BlockState state) {
        Material material = state.getMaterial();
        if (CurtainRules.missingTools && material == Material.GLASS)
            return speed;
        return super.getDestroySpeed(stack, state);
    }
}
