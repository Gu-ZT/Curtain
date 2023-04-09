package dev.dubhe.curtain.features.player.fakes;

import dev.dubhe.curtain.features.player.helpers.EntityPlayerActionPack;

public interface IServerPlayer {
    EntityPlayerActionPack getActionPack();

    void invalidateEntityObjectReference();

    boolean isInvalidEntityObject();

    String getLanguage();
}
