package dev.dubhe.curtain.features.player.helpers;

import dev.dubhe.curtain.features.player.fakes.IServerPlayer;
import dev.dubhe.curtain.features.player.patches.EntityPlayerMPFake;
import dev.dubhe.curtain.utils.Tracer;
import net.minecraft.block.BlockState;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPlayerDiggingPacket;
import net.minecraft.network.play.server.SHeldItemChangePacket;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class EntityPlayerActionPack {
    private final ServerPlayerEntity player;

    private final Map<ActionType, Action> actions = new TreeMap<>();

    private BlockPos currentBlock;
    private int blockHitDelay;
    private boolean isHittingBlock;
    private float curBlockDamageMP;

    private boolean sneaking;
    private boolean sprinting;
    private float forward;
    private float strafing;

    private int itemUseCooldown;

    public EntityPlayerActionPack(ServerPlayerEntity playerIn) {
        player = playerIn;
        stopAll();
    }

    public void copyFrom(EntityPlayerActionPack other) {
        actions.putAll(other.actions);
        currentBlock = other.currentBlock;
        blockHitDelay = other.blockHitDelay;
        isHittingBlock = other.isHittingBlock;
        curBlockDamageMP = other.curBlockDamageMP;

        sneaking = other.sneaking;
        sprinting = other.sprinting;
        forward = other.forward;
        strafing = other.strafing;

        itemUseCooldown = other.itemUseCooldown;
    }

    public EntityPlayerActionPack start(ActionType type, Action action) {
        Action previous = actions.remove(type);
        if (previous != null) type.stop(player, previous);
        if (action != null) {
            actions.put(type, action);
            type.start(player, action); // noop
        }
        return this;
    }

    public EntityPlayerActionPack setSneaking(boolean doSneak) {
        sneaking = doSneak;
        player.setShiftKeyDown(doSneak);
        if (sprinting && sneaking)
            setSprinting(false);
        return this;
    }

    public EntityPlayerActionPack setSprinting(boolean doSprint) {
        sprinting = doSprint;
        player.setSprinting(doSprint);
        if (sneaking && sprinting)
            setSneaking(false);
        return this;
    }

    public EntityPlayerActionPack setForward(float value) {
        forward = value;
        return this;
    }

    public EntityPlayerActionPack setStrafing(float value) {
        strafing = value;
        return this;
    }

    public EntityPlayerActionPack look(Direction direction) {
        switch (direction) {
            case NORTH:
                return look(180, 0);
            case SOUTH:
                return look(0, 0);
            case EAST:
                return look(-90, 0);
            case WEST:
                return look(90, 0);
            case UP:
                return look(player.getYHeadRot(), -90);
            case DOWN:
                return look(player.getYHeadRot(), 90);
        }
        return this;
    }

    public EntityPlayerActionPack look(Vector2f rotation) {
        return look(rotation.x, rotation.y);
    }

    public EntityPlayerActionPack look(float yaw, float pitch) {
        player.setYHeadRot(yaw % 360); //setYaw
        player.setYHeadRot(MathHelper.clamp(pitch, -90, 90)); // setPitch
        // maybe player.setPositionAndAngles(player.x, player.y, player.z, yaw, MathHelper.clamp(pitch,-90.0F, 90.0F));
        return this;
    }

    public EntityPlayerActionPack lookAt(Vector3d position) {
        player.lookAt(EntityAnchorArgument.Type.EYES, position);
        return this;
    }

    public EntityPlayerActionPack turn(float yaw, float pitch) {
        return look(player.yRot + yaw, player.xRot + pitch);
    }

    public EntityPlayerActionPack turn(Vector2f rotation) {
        return turn(rotation.x, rotation.y);
    }

    public EntityPlayerActionPack stopMovement() {
        setSneaking(false);
        setSprinting(false);
        forward = 0.0F;
        strafing = 0.0F;
        return this;
    }


    public EntityPlayerActionPack stopAll() {
        for (ActionType type : actions.keySet()) type.stop(player, actions.get(type));
        actions.clear();
        return stopMovement();
    }

    public EntityPlayerActionPack mount(boolean onlyRideables) {
        //test what happens
        List<Entity> entities;
        if (onlyRideables) {
            entities = player.level.getEntities(player, player.getBoundingBox().inflate(3.0D, 1.0D, 3.0D),
                    e -> e instanceof MinecartEntity || e instanceof BoatEntity || e instanceof AbstractHorseEntity);
        } else {
            entities = player.level.getEntities(player, player.getBoundingBox().inflate(3.0D, 1.0D, 3.0D));
        }
        if (entities.size() == 0)
            return this;
        Entity closest = null;
        double distance = Double.POSITIVE_INFINITY;
        Entity currentVehicle = player.getVehicle();
        for (Entity e : entities) {
            if (e == player || (currentVehicle == e))
                continue;
            double dd = player.distanceToSqr(e);
            if (dd < distance) {
                distance = dd;
                closest = e;
            }
        }
        if (closest == null) return this;
        if (closest instanceof AbstractHorseEntity && onlyRideables)
            ((AbstractHorseEntity) closest).mobInteract(player, Hand.MAIN_HAND);
        else
            player.startRiding(closest, true);
        return this;
    }

    public EntityPlayerActionPack dismount() {
        player.stopRiding();
        return this;
    }

    public void onUpdate() {
        Map<ActionType, Boolean> actionAttempts = new HashMap<>();
        actions.entrySet().removeIf((e) -> e.getValue().done);
        for (Map.Entry<ActionType, Action> e : actions.entrySet()) {
            Action action = e.getValue();
            // skipping attack if use was successful
            if (!(actionAttempts.getOrDefault(ActionType.USE, false) && e.getKey() == ActionType.ATTACK)) {
                Boolean actionStatus = action.tick(this, e.getKey());
                if (actionStatus != null)
                    actionAttempts.put(e.getKey(), actionStatus);
            }
            // optionally retrying use after successful attack and unsuccessful use
            if (e.getKey() == ActionType.ATTACK
                    && actionAttempts.getOrDefault(ActionType.ATTACK, false)
                    && !actionAttempts.getOrDefault(ActionType.USE, true)) {
                // according to MinecraftClient.handleInputEvents
                Action using = actions.get(ActionType.USE);
                if (using != null) // this is always true - we know use worked, but just in case
                {
                    using.retry(this, ActionType.USE);
                }
            }
        }
        float vel = sneaking ? 0.3F : 1.0F;
        // The != 0.0F checks are needed given else real players can't control minecarts, however it works with fakes and else they don't stop immediately
        if (forward != 0.0F || player instanceof EntityPlayerMPFake) {
            player.zza = forward * vel;
        }
        if (strafing != 0.0F || player instanceof EntityPlayerMPFake) {
            player.xxa = strafing * vel;
        }
    }

    static RayTraceResult getTarget(ServerPlayerEntity player) {
        double reach = player.gameMode.isCreative() ? 5 : 4.5f;
        return Tracer.rayTrace(player, 1, reach, false);
    }

    private void dropItemFromSlot(int slot, boolean dropAll) {
        PlayerInventory inv = player.inventory; // getInventory;
        if (!inv.getItem(slot).isEmpty())
            player.drop(inv.removeItem(slot,
                    dropAll ? inv.getItem(slot).getCount() : 1
            ), false, true); // scatter, keep owner
    }

    public void drop(int selectedSlot, boolean dropAll) {
        PlayerInventory inv = player.inventory; // getInventory;
        if (selectedSlot == -2) // all
        {
            for (int i = inv.getContainerSize(); i >= 0; i--)
                dropItemFromSlot(i, dropAll);
        } else // one slot
        {
            if (selectedSlot == -1)
                selectedSlot = inv.selected;
            dropItemFromSlot(selectedSlot, dropAll);
        }
    }

    public void setSlot(int slot) {
        player.inventory.selected = slot - 1;
        player.connection.send(new SHeldItemChangePacket(slot - 1));
    }

    public enum ActionType {
        USE(true) {
            @Override
            boolean execute(ServerPlayerEntity player, Action action) {
                EntityPlayerActionPack ap = ((IServerPlayer) player).getActionPack();
                if (ap.itemUseCooldown > 0) {
                    ap.itemUseCooldown--;
                    return true;
                }
                if (player.isUsingItem()) {
                    return true;
                }
                RayTraceResult hit = getTarget(player);
                for (Hand hand : Hand.values()) {
                    switch (hit.getType()) {
                        case BLOCK: {
                            player.resetLastActionTime();
                            ServerWorld world = player.getLevel();
                            BlockRayTraceResult blockHit = (BlockRayTraceResult) hit;
                            BlockPos pos = blockHit.getBlockPos();
                            Direction side = blockHit.getDirection();
                            if (pos.getY() < player.getLevel().getMaxBuildHeight() - (side == Direction.UP ? 1 : 0) && world.mayInteract(player, pos)) {
                                ActionResultType result = player.gameMode.useItemOn(player, world, player.getItemInHand(hand), hand, blockHit);
                                if (result.consumesAction()) {
                                    if (result.shouldSwing()) player.swing(hand);
                                    ap.itemUseCooldown = 3;
                                    return true;
                                }
                            }
                            break;
                        }
                        case ENTITY: {
                            player.resetLastActionTime();
                            EntityRayTraceResult entityHit = (EntityRayTraceResult) hit;
                            Entity entity = entityHit.getEntity();
                            boolean handWasEmpty = player.getItemInHand(hand).isEmpty();
                            boolean itemFrameEmpty = (entity instanceof ItemFrameEntity frameEntity) && frameEntity.getItem().isEmpty();
                            Vector3d relativeHitPos = entityHit.getLocation().subtract(entity.getX(), entity.getY(), entity.getZ());
                            if (entity.interactAt(player, relativeHitPos, hand).consumesAction()) {
                                ap.itemUseCooldown = 3;
                                return true;
                            }
                            // fix for SS itemframe always returns CONSUME even if no action is performed
                            if (player.interactOn(entity, hand).consumesAction() && !(handWasEmpty && itemFrameEmpty)) {
                                ap.itemUseCooldown = 3;
                                return true;
                            }
                            break;
                        }
                    }
                    ItemStack handItem = player.getItemInHand(hand);
                    if (player.gameMode.useItem(player, player.getLevel(), handItem, hand).consumesAction()) {
                        ap.itemUseCooldown = 3;
                        return true;
                    }
                }
                return false;
            }

            @Override
            void inactiveTick(ServerPlayerEntity player, Action action) {
                EntityPlayerActionPack ap = ((IServerPlayer) player).getActionPack();
                ap.itemUseCooldown = 0;
                player.releaseUsingItem();
            }
        },
        ATTACK(true) {
            @Override
            boolean execute(ServerPlayerEntity player, Action action) {
                RayTraceResult hit = getTarget(player);
                switch (hit.getType()) {
                    case ENTITY: {
                        EntityRayTraceResult entityHit = (EntityRayTraceResult) hit;
                        if (!action.isContinuous) {
                            player.attack(entityHit.getEntity());
                            player.swing(Hand.MAIN_HAND);
                        }
                        player.resetAttackStrengthTicker();
                        player.resetLastActionTime();
                        return true;
                    }
                    case BLOCK: {
                        EntityPlayerActionPack ap = ((IServerPlayer) player).getActionPack();
                        if (!action.isContinuous && player.gameMode.isCreative())
                            ap.blockHitDelay = 0; //Reset delay when attack(once)
                        if (ap.blockHitDelay > 0) {
                            ap.blockHitDelay--;
                            return false;
                        }
                        BlockRayTraceResult blockHit = (BlockRayTraceResult) hit;
                        BlockPos pos = blockHit.getBlockPos();
                        Direction side = blockHit.getDirection();
                        if (player.blockActionRestricted(player.level, pos, player.gameMode.getGameModeForPlayer()))
                            return false;
                        if (ap.currentBlock != null && player.level.getBlockState(ap.currentBlock).isAir()) {
                            ap.currentBlock = null;
                            return false;
                        }
                        BlockState state = player.level.getBlockState(pos);
                        boolean blockBroken = false;
                        if (player.gameMode.getGameModeForPlayer().isCreative()) {
                            player.gameMode.handleBlockBreakAction(pos, CPlayerDiggingPacket.Action.START_DESTROY_BLOCK, side, player.getLevel().getMaxBuildHeight());
                            ap.blockHitDelay = 5;
                            blockBroken = true;
                        } else if (ap.currentBlock == null || !ap.currentBlock.equals(pos)) {
                            if (ap.currentBlock != null) {
                                player.gameMode.handleBlockBreakAction(ap.currentBlock, CPlayerDiggingPacket.Action.ABORT_DESTROY_BLOCK, side, player.getLevel().getMaxBuildHeight());
                            }
                            player.gameMode.handleBlockBreakAction(pos, CPlayerDiggingPacket.Action.START_DESTROY_BLOCK, side, player.getLevel().getMaxBuildHeight());
                            boolean notAir = !state.isAir();
                            if (notAir && ap.curBlockDamageMP == 0) {
                                state.attack(player.level, pos, player);
                            }
                            if (notAir && state.getDestroyProgress(player, player.level, pos) >= 1) {
                                ap.currentBlock = null;
                                //instamine??
                                blockBroken = true;
                            } else {
                                ap.currentBlock = pos;
                                ap.curBlockDamageMP = 0;
                            }
                        } else {
                            ap.curBlockDamageMP += state.getDestroyProgress(player, player.level, pos);
                            if (ap.curBlockDamageMP >= 1) {
                                player.gameMode.handleBlockBreakAction(pos, CPlayerDiggingPacket.Action.STOP_DESTROY_BLOCK, side, player.getLevel().getMaxBuildHeight());
                                ap.currentBlock = null;
                                ap.blockHitDelay = 5;
                                blockBroken = true;
                            }
                            player.level.destroyBlockProgress(-1, pos, (int) (ap.curBlockDamageMP * 10));

                        }
                        player.resetLastActionTime();
                        player.swing(Hand.MAIN_HAND);
                        return blockBroken;
                    }
                }
                return false;
            }

            @Override
            void inactiveTick(ServerPlayerEntity player, Action action) {
                EntityPlayerActionPack ap = ((IServerPlayer) player).getActionPack();
                if (ap.currentBlock == null) return;
                player.level.destroyBlockProgress(-1, ap.currentBlock, -1);
                player.gameMode.handleBlockBreakAction(ap.currentBlock, CPlayerDiggingPacket.Action.ABORT_DESTROY_BLOCK, Direction.DOWN, player.getLevel().getMaxBuildHeight());
                ap.currentBlock = null;
            }
        },
        JUMP(true) {
            @Override
            boolean execute(ServerPlayerEntity player, Action action) {
                if (action.limit == 1) {
                    if (player.isOnGround()) player.jumpFromGround(); // onGround
                } else {
                    player.setJumping(true);
                }
                return false;
            }

            @Override
            void inactiveTick(ServerPlayerEntity player, Action action) {
                player.setJumping(false);
            }
        },
        DROP_ITEM(true) {
            @Override
            boolean execute(ServerPlayerEntity player, Action action) {
                player.resetLastActionTime();
                player.drop(false); // dropSelectedItem
                return false;
            }
        },
        DROP_STACK(true) {
            @Override
            boolean execute(ServerPlayerEntity player, Action action) {
                player.resetLastActionTime();
                player.drop(true); // dropSelectedItem
                return false;
            }
        },
        SWAP_HANDS(true) {
            @Override
            boolean execute(ServerPlayerEntity player, Action action) {
                player.resetLastActionTime();
                ItemStack itemStack_1 = player.getItemInHand(Hand.OFF_HAND);
                player.setItemInHand(Hand.OFF_HAND, player.getItemInHand(Hand.MAIN_HAND));
                player.setItemInHand(Hand.MAIN_HAND, itemStack_1);
                return false;
            }
        };

        public final boolean preventSpectator;

        ActionType(boolean preventSpectator) {
            this.preventSpectator = preventSpectator;
        }

        void start(ServerPlayerEntity player, Action action) {
        }

        abstract boolean execute(ServerPlayerEntity player, Action action);

        void inactiveTick(ServerPlayerEntity player, Action action) {
        }

        void stop(ServerPlayerEntity player, Action action) {
            inactiveTick(player, action);
        }
    }

    public static class Action {
        public boolean done = false;
        public final int limit;
        public final int interval;
        public final int offset;
        private int count;
        private int next;
        private final boolean isContinuous;

        private Action(int limit, int interval, int offset, boolean continuous) {
            this.limit = limit;
            this.interval = interval;
            this.offset = offset;
            next = interval + offset;
            isContinuous = continuous;
        }

        public static Action once() {
            return new Action(1, 1, 0, false);
        }

        public static Action continuous() {
            return new Action(-1, 1, 0, true);
        }

        public static Action interval(int interval) {
            return new Action(-1, interval, 0, false);
        }

        public static Action interval(int interval, int offset) {
            return new Action(-1, interval, offset, false);
        }

        Boolean tick(EntityPlayerActionPack actionPack, ActionType type) {
            next--;
            Boolean cancel = null;
            if (next <= 0) {
                if (interval == 1 && !isContinuous) {
                    // need to allow entity to tick, otherwise won't have effect (bow)
                    // actions are 20 tps, so need to clear status mid tick, allowing entities process it till next time
                    if (!type.preventSpectator || !actionPack.player.isSpectator()) {
                        type.inactiveTick(actionPack.player, this);
                    }
                }

                if (!type.preventSpectator || !actionPack.player.isSpectator()) {
                    cancel = type.execute(actionPack.player, this);
                }
                count++;
                if (count == limit) {
                    type.stop(actionPack.player, null);
                    done = true;
                    return cancel;
                }
                next = interval;
            } else {
                if (!type.preventSpectator || !actionPack.player.isSpectator()) {
                    type.inactiveTick(actionPack.player, this);
                }
            }
            return cancel;
        }

        void retry(EntityPlayerActionPack actionPack, ActionType type) {
            //assuming action run but was unsuccesful that tick, but opportunity emerged to retry it, lets retry it.
            if (!type.preventSpectator || !actionPack.player.isSpectator()) {
                type.execute(actionPack.player, this);
            }
            count++;
            if (count == limit) {
                type.stop(actionPack.player, null);
                done = true;
            }
        }
    }
}
