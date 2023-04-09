package dev.dubhe.curtain.features.logging.helper;

import dev.dubhe.curtain.features.logging.AbstractLogger;
import dev.dubhe.curtain.features.logging.LoggerManager;
import dev.dubhe.curtain.utils.Messenger;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static dev.dubhe.curtain.utils.Messenger.c;

public class ExplosionLogHelper {
    private final boolean createFire;
    private final Explosion.BlockInteraction blockDestructionType;
    private final RegistryAccess regs;
    public final Vec3 pos;
    private final float power;
    private boolean affectBlocks = false;
    private final Object2IntMap<EntityChangedStatusWithCount> impactedEntities = new Object2IntOpenHashMap<>();

    private static long lastGametime = 0;
    private static int explosionCountInCurrentGT = 0;
    private static boolean newTick;

    private static Component log;

    public ExplosionLogHelper(double x, double y, double z, float power, boolean createFire, Explosion.BlockInteraction blockDestructionType, RegistryAccess regs) {
        this.power = power;
        this.pos = new Vec3(x, y, z);
        this.createFire = createFire;
        this.blockDestructionType = blockDestructionType;
        this.regs = regs;
    }

    public void setAffectBlocks(boolean b) {
        affectBlocks = b;
    }

    public void onExplosionDone(long gametime) {
        newTick = false;
        if (!(lastGametime == gametime)) {
            explosionCountInCurrentGT = 0;
            lastGametime = gametime;
            newTick = true;
        }
        explosionCountInCurrentGT++;

        List<Component> messages = new ArrayList<>();
        if (newTick) messages.add(c("wb tick : ", "d " + gametime));

        messages.add(c("d #" + explosionCountInCurrentGT, "gb ->", Messenger.dblt("l", pos.x, pos.y, pos.z)));
        messages.add(c("w   affects blocks: ", "m " + this.affectBlocks));
        messages.add(c("w   creates fire: ", "m " + this.createFire));
        messages.add(c("w   power: ", "c " + this.power));
        messages.add(c("w   destruction: ", "c " + this.blockDestructionType.name()));
        if (impactedEntities.isEmpty()) {
            messages.add(c("w   affected entities: ", "m None"));
        } else {
            messages.add(c("w   affected entities:"));
            impactedEntities.forEach((k, v) ->
            {
                messages.add(c((k.pos.equals(pos)) ? "r   - TNT" : "w   - ",
                        Messenger.dblt((k.pos.equals(pos)) ? "r" : "y", k.pos.x, k.pos.y, k.pos.z), "w  dV",
                        Messenger.dblt("d", k.accel.x, k.accel.y, k.accel.z),
                        "w  " + regs.registryOrThrow(ForgeRegistries.ENTITY_TYPES.getRegistryKey()).getKey(k.type).getPath(), (v > 1) ? "l (" + v + ")" : ""
                ));
            });
        }

        Iterator<Component> iterator = messages.iterator();
        MutableComponent rt = Component.empty();
        for (Component component = iterator.next(); iterator.hasNext(); component = iterator.next()) {
            rt.append(component);
            if (iterator.hasNext()) {
                rt.append(Component.literal("\n"));
            }
        }
        log = rt;
        LoggerManager.ableSendToChat("explosion");
    }

    public void onEntityImpacted(Entity entity, Vec3 accel) {
        EntityChangedStatusWithCount ent = new EntityChangedStatusWithCount(entity, accel);
        impactedEntities.put(ent, impactedEntities.getOrDefault(ent, 0) + 1);
    }

    public static class ExplosionLogger extends AbstractLogger {
        public ExplosionLogger() {
            super("explosion");
        }

        @Override
        public Component display(ServerPlayer player) {
            return log;
        }
    }


    public record EntityChangedStatusWithCount(Vec3 pos, EntityType<?> type, Vec3 accel) {
        public EntityChangedStatusWithCount(Entity e, Vec3 accel) {
            this(e.position(), e.getType(), accel);
        }
    }
}
