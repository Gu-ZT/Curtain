package dev.dubhe.curtain.api.rules;

import java.util.Locale;

public class RuleHelper {
    public static String toRuleString(Object value) {
        if (value instanceof Enum) return ((Enum<?>) value).name().toLowerCase(Locale.ROOT);
        return value.toString();
    }
}
