package dev.dubhe.curtain;

import dev.dubhe.curtain.api.rules.CurtainRule;
import dev.dubhe.curtain.api.rules.Rule;
import dev.dubhe.curtain.api.rules.Validator;
import dev.dubhe.curtain.api.rules.Validators;
import dev.dubhe.curtain.utils.TranslationHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerInterface;

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
    
    public static class ViewDistanceValidator implements Validator<Integer> {
        @Override
        public boolean validate(CommandSourceStack source, CurtainRule<Integer> rule, String newValue) {
            int value;
            try {
                value = Integer.parseInt(newValue);
            } catch (NumberFormatException e) {
                return false;
            }
            if (value < 0 || value > 32) {
                source.sendFailure(Component.literal("view distance has to be between 0 and 32"));
                return false;
            }
            MinecraftServer server = source.getServer();
            if (server.isDedicatedServer()) {
                int vd = (value > 2) ? value : ((ServerInterface) server).getProperties().viewDistance;
                if (vd != server.getPlayerList().getViewDistance()) {
                    server.getPlayerList().setViewDistance(vd);
                }

                return true;
            } else {
                source.sendFailure(Component.literal("view distance can only be changed on a server"));
                return false;
            }
        }
    }

    @Rule(
            categories = CREATIVE,
            validators = ViewDistanceValidator.class,
            suggestions = {"0", "12", "16", "32"}
    )
    public static Integer viewDistance = 0;

    @Rule(
            categories = {CREATIVE},
            suggestions = {"true","false"}
    )
    public static Boolean xpNoCooldown = false;

    @Rule(
            categories = {COMMAND},
            suggestions = {"true","false"}
    )
    public static Boolean allowSpawningOfflinePlayers = false;

    @Rule(
            categories = {COMMAND},
            validators = {Validators.CommandLevel.class},
            suggestions = {"ops", "true", "false"}
    )
    public static String commandPlayer = "ops";
}
