package dev.dubhe.curtain;

import dev.dubhe.curtain.api.rules.CurtainRule;
import dev.dubhe.curtain.api.rules.Rule;
import dev.dubhe.curtain.api.rules.Validator;
import dev.dubhe.curtain.utils.TranslationHelper;
import net.minecraft.commands.CommandSourceStack;

import static dev.dubhe.curtain.api.rules.Categories.*;

public class CurtainRules {
    public static class LanguageValidator implements Validator<String> {
        @Override
        public boolean validate(CommandSourceStack source, CurtainRule<String> rule, String newValue) {
            return TranslationHelper.getLanguages().contains(newValue);
        }
    }

    @Rule(
            categories = {FEATURE},
            validators = {LanguageValidator.class},
            suggestions = {"zh_cn", "en_us"}
    )
    public static String language = "zh_cn";
}
