package dev.dubhe.curtain.mixins.rules.fill_updates;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Commands.class)
public abstract class CommandsMixin {
    @Inject(method = "performCommand", at = @At("HEAD"))
    private void onExecuteBegin(CommandSource p_82118_, String p_82119_, CallbackInfoReturnable<Integer> cir) {
        if (!CurtainRules.fillUpdates) {
            CurtainRules.impendingFillSkipUpdates.set(true);
        }
    }

    @Inject(method = "performCommand", at = @At("RETURN"))
    private void onExecuteEnd(CommandSource p_82118_, String p_82119_, CallbackInfoReturnable<Integer> cir) {
        if (!CurtainRules.fillUpdates) {
            CurtainRules.impendingFillSkipUpdates.set(false);
        }
    }
}
