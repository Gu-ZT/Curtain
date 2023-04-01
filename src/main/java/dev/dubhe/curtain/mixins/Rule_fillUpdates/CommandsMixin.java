package dev.dubhe.curtain.mixins.Rule_fillUpdates;

import com.mojang.brigadier.ParseResults;
import dev.dubhe.curtain.CurtainRules;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Commands.class)
public class CommandsMixin {
    @Inject(method = "performCommand", at = @At("HEAD"))
    private void onExecuteBegin(ParseResults<CommandSourceStack> parseResults, String string, CallbackInfoReturnable<Integer> cir) {
        if (!CurtainRules.fillUpdates) {
            CurtainRules.impendingFillSkipUpdates.set(true);
        }
    }

    @Inject(method = "performCommand", at = @At("RETURN"))
    private void onExecuteEnd(ParseResults<CommandSourceStack> parseResults, String string, CallbackInfoReturnable<Integer> cir) {
        if (!CurtainRules.fillUpdates) {
            CurtainRules.impendingFillSkipUpdates.set(false);
        }
    }
}
