package dev.dubhe.curtain.events.events;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

public class ItemStackEvent extends Event {
    private final ItemStack stack;

    protected ItemStackEvent(ItemStack stack) {
        this.stack = stack;
    }

    public ItemStack getStack() {
        return stack;
    }

    @Cancelable
    public static class Use extends ItemStackEvent {
        private final World level;
        private final PlayerEntity player;
        private final Hand usedHand;

        public Use(ItemStack stack, World level, PlayerEntity player, Hand usedHand) {
            super(stack);
            this.level = level;
            this.player = player;
            this.usedHand = usedHand;
        }

        public World getLevel() {
            return level;
        }

        public PlayerEntity getPlayer() {
            return player;
        }

        public Hand getUsedHand() {
            return usedHand;
        }
    }

    @Cancelable
    public static class HurtAndBreak extends ItemStackEvent {
        private final int amount;
        private final PlayerEntity player;

        public HurtAndBreak(ItemStack stack, int amount, PlayerEntity player) {
            super(stack);
            this.amount = amount;
            this.player = player;
        }

        public int getAmount() {
            return amount;
        }

        public PlayerEntity getPlayer() {
            return player;
        }
    }

    @Cancelable
    public static class BreakSpeed extends ItemStackEvent {
        private final BlockState state;
        private final float originalSpeed;
        private float speed;

        public BreakSpeed(ItemStack stack, BlockState state, float originalSpeed, float speed) {
            super(stack);
            this.state = state;
            this.originalSpeed = originalSpeed;
            this.speed = speed;
        }

        public BlockState getState() {
            return state;
        }

        public float getOriginalSpeed() {
            return originalSpeed;
        }

        public float getSpeed() {
            return speed;
        }

        public void setSpeed(float speed) {
            this.speed = speed;
        }
    }
}
