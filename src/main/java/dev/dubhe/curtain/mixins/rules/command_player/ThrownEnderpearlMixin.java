package dev.dubhe.curtain.mixins.rules.command_player;

import dev.dubhe.curtain.features.player.patches.EntityPlayerMPFake;
import net.minecraft.network.Connection;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ThrownEnderpearl.class)
public abstract class ThrownEnderpearlMixin extends ThrowableItemProjectile {
    public ThrownEnderpearlMixin(EntityType<? extends ThrowableItemProjectile> entityType_1, Level world_1) {
        super(entityType_1, world_1);
    }

    @Redirect(method = "onHit", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/Connection;isConnected()Z"
    ))
    private boolean isConnectionGood(Connection instance) {
        return instance.isConnected() || getOwner() instanceof EntityPlayerMPFake;
    }
}
