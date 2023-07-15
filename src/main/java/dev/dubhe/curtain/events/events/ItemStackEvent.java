package dev.dubhe.curtain.events.events;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
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
