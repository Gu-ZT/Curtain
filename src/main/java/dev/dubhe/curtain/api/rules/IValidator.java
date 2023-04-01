package dev.dubhe.curtain.api.rules;

import net.minecraft.commands.CommandSourceStack;

@FunctionalInterface
public interface IValidator<T> {
    boolean validate(CommandSourceStack source, CurtainRule<T> rule, String newValue);
}
