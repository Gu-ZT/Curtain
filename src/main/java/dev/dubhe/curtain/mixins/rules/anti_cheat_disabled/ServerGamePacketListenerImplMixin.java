package dev.dubhe.curtain.mixins.rules.anti_cheat_disabled;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.IServerPlayNetHandler;
import net.minecraft.network.play.ServerPlayNetHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetHandler.class)
public abstract class ServerGamePacketListenerImplMixin implements IServerPlayNetHandler {
    @Shadow
    private int aboveGroundTickCount;

    @Shadow
    private int aboveGroundVehicleTickCount;

    @Shadow
    protected abstract boolean isSingleplayerOwner();

    @Inject(method = "tick", at = @At("HEAD"))
    private void restrictFloatingBits(CallbackInfo ci) {
        if (CurtainRules.antiCheatDisabled) {
            if (aboveGroundTickCount > 70) aboveGroundTickCount--;
            if (aboveGroundVehicleTickCount > 70) aboveGroundVehicleTickCount--;
        }

    }

    @Redirect(method = "handleMoveVehicle", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/play/ServerPlayNetHandler;isSingleplayerOwner()Z"
    ))
    private boolean isServerTrusting(ServerPlayNetHandler instance) {
        return isSingleplayerOwner() || CurtainRules.antiCheatDisabled;
    }

    @Redirect(method = "handleMovePlayer", require = 0, // don't crash with immersive portals,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/ServerPlayerEntity;isChangingDimension()Z"))
    private boolean relaxMoveRestrictions(ServerPlayerEntity instance) {
        return CurtainRules.antiCheatDisabled || instance.isChangingDimension();
    }
}
