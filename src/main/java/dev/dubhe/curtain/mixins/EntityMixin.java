package dev.dubhe.curtain.mixins;

import dev.dubhe.curtain.features.player.fakes.IEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Entity.class)
public abstract class EntityMixin implements IEntity {
    @Shadow
    private float yRot;

    @Shadow
    public float yRotO;


    @Shadow
    public Level level;

    @Override
    public float getMainYaw(float partialTicks) {
        return partialTicks == 1.0F ? this.yRot : Mth.lerp(partialTicks, this.yRotO, this.yRot);
    }
}
