package dev.dubhe.curtain.mixins.rules.command_player;

import dev.dubhe.curtain.features.player.patches.EntityPlayerMPFake;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.EnderPearlEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.network.NetworkManager;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EnderPearlEntity.class)
public abstract class ThrownEnderpearlMixin extends ProjectileItemEntity {
    public ThrownEnderpearlMixin(EntityType<? extends ProjectileItemEntity> entityType_1, World world_1) {
        super(entityType_1, world_1);
    }

    @Redirect(method = "onHit", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/NetworkManager;isConnected()Z"
    ))
    private boolean isConnectionGood(NetworkManager instance) {
        return instance.isConnected() || getOwner() instanceof EntityPlayerMPFake;
    }
}
