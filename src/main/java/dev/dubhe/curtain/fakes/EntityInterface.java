package dev.dubhe.curtain.fakes;

public interface EntityInterface {
    float getMainYaw(float partialTicks);

    boolean isPermanentVehicle();

    void setPermanentVehicle(boolean permanent);

    int getPortalTimer();

    void setPortalTimer(int amount);

    int getPublicNetherPortalCooldown();
    void setPublicNetherPortalCooldown(int what);
}
