package dev.dubhe.curtain.features.player.fakes;

public interface IEntity {
    float getMainYaw(float partialTicks);

    boolean isPermanentVehicle();

    void setPermanentVehicle(boolean permanent);

    int getPortalTimer();

    void setPortalTimer(int amount);

    int getPublicNetherPortalCooldown();

    void setPublicNetherPortalCooldown(int what);
}
