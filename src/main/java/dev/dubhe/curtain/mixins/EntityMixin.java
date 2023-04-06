package dev.dubhe.curtain.mixins;

import dev.dubhe.curtain.features.player.fakes.IEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Entity.class)
public abstract class EntityMixin implements IEntity {
    @Shadow
    private float yRot;

    @Shadow
    public float yRotO;


    @Shadow
    public World level;

    @Override
    public float getMainYaw(float partialTicks) {
        return partialTicks == 1.0F ? this.yRot : MathHelper.lerp(partialTicks, this.yRotO, this.yRot);
    }
}
