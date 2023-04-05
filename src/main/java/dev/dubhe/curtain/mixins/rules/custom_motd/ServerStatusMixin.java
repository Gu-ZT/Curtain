package dev.dubhe.curtain.mixins.rules.custom_motd;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.status.ServerStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerStatus.class)
public abstract class ServerStatusMixin {
    @Inject(method = "description", at = @At("HEAD"), cancellable = true)
    private void getDescriptionAlternative(CallbackInfoReturnable<Component> cir) {
        if (!CurtainRules.customMOTD.contentEquals("none")) {
            cir.setReturnValue(Component.literal(CurtainRules.customMOTD));
            cir.cancel();
        }
    }
}
