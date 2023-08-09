package dev.dubhe.curtain.mixins;

import net.minecraft.util.WeightedRandom;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WeightedRandom.Item.class)
public interface WeightedRandomItemMixin {
    @Accessor("weight")
    int getWeight();
}
