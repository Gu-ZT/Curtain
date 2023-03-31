package dev.dubhe.curtain.api.rules;

import dev.dubhe.curtain.Curtain;

public class RuleException extends RuntimeException {
    public RuleException(String msg) {
        super(msg);
        Curtain.LOGGER.error(msg);
    }

    public static RuleException type() {
        return new RuleException("Curtain rule must be a number, boolean, or string");
    }

    public static RuleException nu11() {
        return new RuleException("Curtain rule do not exist");
    }

    public static RuleException legal() {
        return new RuleException("This is not a legal rule value");
    }
}
