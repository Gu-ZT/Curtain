package dev.dubhe.curtain.api.rules;

import net.minecraft.command.CommandSource;

@FunctionalInterface
public interface IValidator<T> {
    boolean validate(CommandSource source, CurtainRule<T> rule, String newValue);
}
