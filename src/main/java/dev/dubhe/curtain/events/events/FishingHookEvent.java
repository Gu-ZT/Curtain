package dev.dubhe.curtain.events.events;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.EntityEvent;

public class FishingHookEvent extends EntityEvent {
    private final PlayerEntity owner;

    public FishingHookEvent(FishingBobberEntity entity) {
        super(entity);
        owner = entity.getPlayerOwner();
    }

    public PlayerEntity getOwner() {
        return owner;
    }

    public static class Catching extends FishingHookEvent {
        private final BlockPos pos;

        public Catching(FishingBobberEntity entity, BlockPos pos) {
            super(entity);
            this.pos = pos;
        }

        public BlockPos getPos() {
            return pos;
        }
    }
}
