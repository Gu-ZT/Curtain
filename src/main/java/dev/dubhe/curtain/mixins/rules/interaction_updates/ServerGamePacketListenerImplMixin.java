package dev.dubhe.curtain.mixins.rules.interaction_updates;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.network.play.ServerPlayNetHandler;
import net.minecraft.network.play.client.CPlayerTryUseItemOnBlockPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetHandler.class)
public abstract class ServerGamePacketListenerImplMixin {
    @Inject(method = "handleUseItemOn",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/management/PlayerInteractionManager;useItemOn(Lnet/minecraft/entity/player/ServerPlayerEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/Hand;Lnet/minecraft/util/math/BlockRayTraceResult;)Lnet/minecraft/util/ActionResultType;",
                    shift = At.Shift.BEFORE
            )
    )
    private void beforeBlockInteracted(CPlayerTryUseItemOnBlockPacket packet, CallbackInfo ci) {
        if (!CurtainRules.interactionUpdates) {
            CurtainRules.impendingFillSkipUpdates.set(true);
        }
    }

    @Inject(
            method = "handleUseItemOn",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/management/PlayerInteractionManager;useItemOn(Lnet/minecraft/entity/player/ServerPlayerEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/Hand;Lnet/minecraft/util/math/BlockRayTraceResult;)Lnet/minecraft/util/ActionResultType;",
                    shift = At.Shift.AFTER
            )
    )
    private void afterBlockInteracted(CPlayerTryUseItemOnBlockPacket packet, CallbackInfo ci) {
        if (!CurtainRules.interactionUpdates) {
            CurtainRules.impendingFillSkipUpdates.set(false);
        }
    }

    @Inject(
            method = "handleUseItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayerGameMode;useItem(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;",
                    shift = At.Shift.BEFORE
            )
    )
    private void beforeItemInteracted(ServerboundUseItemPacket packet, CallbackInfo ci) {
        if (!CurtainRules.interactionUpdates) {
            CurtainRules.impendingFillSkipUpdates.set(true);
        }
    }

    @Inject(
            method = "handleUseItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayerGameMode;useItem(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;",
                    shift = At.Shift.AFTER
            )
    )
    private void afterItemInteracted(ServerboundUseItemPacket packet, CallbackInfo ci) {
        if (!CurtainRules.interactionUpdates) {
            CurtainRules.impendingFillSkipUpdates.set(false);
        }
    }

    @Inject(
            method = "handlePlayerAction",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayerGameMode;handleBlockBreakAction(Lnet/minecraft/core/BlockPos;Lnet/minecraft/network/protocol/game/ServerboundPlayerActionPacket$Action;Lnet/minecraft/core/Direction;I)V",
                    shift = At.Shift.BEFORE
            )
    )
    private void beforeBlockBroken(ServerboundPlayerActionPacket packet, CallbackInfo ci) {
        if (!CurtainRules.interactionUpdates) {
            CurtainRules.impendingFillSkipUpdates.set(true);
        }
    }

    @Inject(
            method = "handlePlayerAction",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayerGameMode;handleBlockBreakAction(Lnet/minecraft/core/BlockPos;Lnet/minecraft/network/protocol/game/ServerboundPlayerActionPacket$Action;Lnet/minecraft/core/Direction;I)V",
                    shift = At.Shift.AFTER
            )
    )
    private void afterBlockBroken(ServerboundPlayerActionPacket packet, CallbackInfo ci) {
        if (!CurtainRules.interactionUpdates) {
            CurtainRules.impendingFillSkipUpdates.set(false);
        }
    }
}
