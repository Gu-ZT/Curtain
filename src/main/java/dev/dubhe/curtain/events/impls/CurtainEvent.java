package dev.dubhe.curtain.events.impls;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

public class CurtainEvent extends Event {

    @Cancelable
    public static class BreakSpeed extends CurtainEvent {
        private final ItemStack stack;
        private final BlockState state;
        private final float originalSpeed;
        private float speed;

        public BreakSpeed(ItemStack stack, BlockState state, float originalSpeed, float speed) {
            super();
            this.stack = stack;
            this.state = state;
            this.originalSpeed = originalSpeed;
            this.speed = speed;
        }

        public ItemStack getStack() {
            return stack;
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
