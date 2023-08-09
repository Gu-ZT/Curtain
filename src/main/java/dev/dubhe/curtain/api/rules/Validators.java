package dev.dubhe.curtain.api.rules;

import dev.dubhe.curtain.utils.CommandHelper;
import net.minecraft.command.CommandSource;

import java.util.ArrayList;
import java.util.List;

public final class Validators {
    private Validators() {
    }

    public static class CommandLevel implements IValidator<String> {
        public static final List<String> OPTIONS = new ArrayList<>();

        static {
            OPTIONS.add("true");
            OPTIONS.add("false");
            OPTIONS.add("ops");
            OPTIONS.add("0");
            OPTIONS.add("1");
            OPTIONS.add("2");
            OPTIONS.add("3");
            OPTIONS.add("4");
        }

        @Override
        public boolean validate(CommandSource source, CurtainRule<String> rule, String newValue) {
            boolean is_valid = OPTIONS.contains(newValue);
            if (source != null && is_valid)
                CommandHelper.notifyPlayersCommandsChanged(source.getServer());
            return is_valid;
        }
    }
}
