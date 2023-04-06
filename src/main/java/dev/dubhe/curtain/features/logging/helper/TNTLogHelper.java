package dev.dubhe.curtain.features.logging.helper;

import dev.dubhe.curtain.features.logging.AbstractLogger;
import dev.dubhe.curtain.features.logging.LoggerManager;
import dev.dubhe.curtain.utils.Messenger;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;


public class TNTLogHelper {
    public boolean initialized;
    private double primedX, primedY, primedZ;
    private static long lastGametime = 0;
    private static int tntCount = 0;
    private Vector3d primedAngle;
    private static ITextComponent log;

    /**
     * Runs when the TNT is primed. Expects the position and motion angle of the TNT.
     */
    public void onPrimed(double x, double y, double z, Vector3d motion) {
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
        public ITextComponent display(ServerPlayerEntity player) {
            return log;
        }
    }
}
