package dev.dubhe.curtain.features.logging.helper;

import dev.dubhe.curtain.features.logging.AbstractLogger;
import dev.dubhe.curtain.features.logging.LoggerManager;
import dev.dubhe.curtain.utils.Messenger;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

public class TNTLogHelper {
    public boolean initialized;
    private double primedX, primedY, primedZ;
    private static long lastGametime = 0;
    private static int tntCount = 0;
    private Vec3 primedAngle;
    private static Component log;

    /**
     * Runs when the TNT is primed. Expects the position and motion angle of the TNT.
     */
    public void onPrimed(double x, double y, double z, Vec3 motion) {
        primedX = x;
        primedY = y;
        primedZ = z;
        primedAngle = motion;
        initialized = true;
    }

    /**
     * Runs when the TNT explodes. Expects the position of the TNT.
     */
    public void onExploded(double x, double y, double z, long gametime) {
        if (!(lastGametime == gametime)) {
            tntCount = 0;
            lastGametime = gametime;
        }
        tntCount++;

        log = Messenger.c(
                "r #" + tntCount,
                "m @" + gametime,
                "g : ",
                "l P ", Messenger.dblf("l", primedX, primedY, primedZ),
                "w  ", Messenger.dblf("l", primedAngle.x, primedAngle.y, primedAngle.z),
                "r  E ", Messenger.dblf("r", x, y, z));
        LoggerManager.ableSendToChat("tnt");
    }

    public static class TNTLogger extends AbstractLogger {

        public TNTLogger() {
            super("tnt");
        }

        @Override
        public Component display(ServerPlayer player) {
            return log;
        }
    }
}
