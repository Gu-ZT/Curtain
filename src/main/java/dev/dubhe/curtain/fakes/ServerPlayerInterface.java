package dev.dubhe.curtain.fakes;

import dev.dubhe.curtain.helpers.EntityPlayerActionPack;

public interface ServerPlayerInterface {
    EntityPlayerActionPack getActionPack();
    void invalidateEntityObjectReference();
    boolean isInvalidEntityObject();
    String getLanguage();
}
