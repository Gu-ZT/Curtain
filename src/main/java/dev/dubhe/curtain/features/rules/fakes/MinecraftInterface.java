package dev.dubhe.curtain.features.rules.fakes;

import dev.dubhe.curtain.utils.TickRateManager;

import java.util.Optional;

public interface MinecraftInterface
{
    Optional<TickRateManager> getTickRateManager();
}
