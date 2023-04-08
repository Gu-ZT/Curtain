package dev.dubhe.curtain.mixins;


import net.minecraft.entity.Entity;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Random;

@Mixin(Explosion.class)
public interface ExplosionAccessor {
    @Accessor
    boolean isFire();

    @Accessor
    Explosion.Mode getBlockInteraction();

    @Accessor
    World getLevel();

    @Accessor
    Random getRandom();

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
