package dev.dubhe.curtain.mixins.rules.custom_motd;

import dev.dubhe.curtain.CurtainRules;

import net.minecraft.network.ServerStatusResponse;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerStatusResponse.class)
public abstract class ServerStatusMixin {
    @Inject(method = "getDescription", at = @At("HEAD"), cancellable = true)
    private void getDescriptionAlternative(CallbackInfoReturnable<ITextComponent> cir) {
        if (!CurtainRules.customMOTD.contentEquals("none")) {
            cir.setReturnValue(new StringTextComponent(CurtainRules.customMOTD));
            cir.cancel();
        }
    }
}
