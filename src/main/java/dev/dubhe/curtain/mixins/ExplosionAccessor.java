package dev.dubhe.curtain.mixins;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Explosion.class)
public interface ExplosionAccessor {
    @Accessor
    boolean isFire();

    @Accessor
    Explosion.BlockInteraction getBlockInteraction();

    @Accessor
    Level getLevel();

    @Accessor
    RandomSource getRandom();

    @Accessor("x")
    double getX();

    @Accessor("y")
    double getY();

    @Accessor("z")
    double getZ();

    @Accessor
    float getRadius();

    @Accessor
    Entity getSource();
}
