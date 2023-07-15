package dev.dubhe.curtain.events.events;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

import java.util.function.Consumer;

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
        private final Level level;
        private final Player player;
        private final InteractionHand usedHand;

        public Use(ItemStack stack, Level level, Player player, InteractionHand usedHand) {
            super(stack);
            this.level = level;
            this.player = player;
            this.usedHand = usedHand;
        }

        public Level getLevel() {
            return level;
        }

        public Player getPlayer() {
            return player;
        }

        public InteractionHand getUsedHand() {
            return usedHand;
        }
    }

    @Cancelable
    public static class HurtAndBreak extends ItemStackEvent {
        private final int amount;
        private final Player player;

        public HurtAndBreak(ItemStack stack, int amount, Player player) {
            super(stack);
            this.amount = amount;
            this.player = player;
        }

        public int getAmount() {
            return amount;
        }

        public Player getPlayer() {
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
