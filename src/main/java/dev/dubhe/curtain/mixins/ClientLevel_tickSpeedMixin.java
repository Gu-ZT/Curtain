package dev.dubhe.curtain.mixins;

import dev.dubhe.curtain.features.rules.fakes.LevelInterface;
import dev.dubhe.curtain.utils.TickRateManager;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public abstract class ClientLevel_tickSpeedMixin implements LevelInterface
{

    private TickRateManager tickRateManager;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(CallbackInfo ci)
    {
        this.tickRateManager = new TickRateManager();
    }

    @Override
    public TickRateManager tickRateManager()
    {
        return tickRateManager;
    }
}
