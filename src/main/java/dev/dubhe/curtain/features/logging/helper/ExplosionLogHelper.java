package dev.dubhe.curtain.features.logging.helper;

import dev.dubhe.curtain.features.logging.AbstractLogger;
import dev.dubhe.curtain.features.logging.LoggerManager;
import dev.dubhe.curtain.utils.Messenger;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.world.Explosion;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static dev.dubhe.curtain.utils.Messenger.c;

public class ExplosionLogHelper {
    private final boolean createFire;
    private final Explosion.Mode blockDestructionType;
    private final DynamicRegistries regs;
    public final Vector3d pos;
    private final float power;
    private boolean affectBlocks = false;
    private final Object2IntMap<EntityChangedStatusWithCount> impactedEntities = new Object2IntOpenHashMap<>();

    private static long lastGametime = 0;
    private static int explosionCountInCurrentGT = 0;
    private static boolean newTick;

    private static ITextComponent log;

    public ExplosionLogHelper(double x, double y, double z, float power, boolean createFire, Explosion.Mode blockDestructionType, DynamicRegistries regs) {
        this.power = power;
        this.pos = new Vector3d(x, y, z);
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

        List<ITextComponent> messages = new ArrayList<>();
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
                        "w  " + ForgeRegistries.ENTITIES.getKey(k.type).getPath(), (v > 1) ? "l (" + v + ")" : ""
                ));
            });
        }

        Iterator<ITextComponent> iterator = messages.iterator();
        TextComponent rt = new StringTextComponent("");
        for (ITextComponent component = iterator.next(); iterator.hasNext(); component = iterator.next()) {
            rt.append(component);
            if (iterator.hasNext()) {
                rt.append(new StringTextComponent("\n"));
            }
        }
        log = rt;
        LoggerManager.ableSendToChat("explosion");
    }

    public void onEntityImpacted(Entity entity, Vector3d accel) {
        EntityChangedStatusWithCount ent = new EntityChangedStatusWithCount(entity, accel);
        impactedEntities.put(ent, impactedEntities.getOrDefault(ent, 0) + 1);
    }

    public static class ExplosionLogger extends AbstractLogger {
        public ExplosionLogger() {
            super("explosion");
        }

        @Override
        public ITextComponent display(ServerPlayerEntity player) {
            return log;
        }
    }


    public record EntityChangedStatusWithCount(Vector3d pos, EntityType<?> type, Vector3d accel) {
        public EntityChangedStatusWithCount(Entity e, Vector3d accel) {
            this(e.position(), e.getType(), accel);
        }
    }
}
