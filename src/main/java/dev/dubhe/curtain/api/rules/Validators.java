package dev.dubhe.curtain.api.rules;

import dev.dubhe.curtain.utils.CommandHelper;
import net.minecraft.commands.CommandSourceStack;

import java.util.List;

public final class Validators {
    private Validators() {
    }

    public static class CommandLevel implements IValidator<String> {
        public static final List<String> OPTIONS = List.of("true", "false", "ops", "0", "1", "2", "3", "4");

        @Override
        public boolean validate(CommandSourceStack source, CurtainRule<String> rule, String newValue) {
            boolean is_valid = OPTIONS.contains(newValue);
            if (source != null && is_valid)
                CommandHelper.notifyPlayersCommandsChanged(source.getServer());
            return is_valid;
        }
    }
}
