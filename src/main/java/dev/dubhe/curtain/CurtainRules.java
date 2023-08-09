package dev.dubhe.curtain;

import dev.dubhe.curtain.api.rules.CurtainRule;
import dev.dubhe.curtain.api.rules.IValidator;
import dev.dubhe.curtain.api.rules.Rule;
import dev.dubhe.curtain.api.rules.Validators;
import dev.dubhe.curtain.utils.TranslationHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerInterface;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import static dev.dubhe.curtain.api.rules.Categories.*;


@SuppressWarnings("unused")
public class CurtainRules {
    public static final ThreadLocal<Boolean> impendingFillSkipUpdates = ThreadLocal.withInitial(() -> false);

    public static class LanguageValidator implements IValidator<String> {
        @Override
        public boolean validate(CommandSourceStack source, CurtainRule<String> rule, String newValue) {
            return TranslationHelper.getLanguages().contains(newValue);
        }
    }

    @Rule(
            categories = FEATURE,
            validators = LanguageValidator.class,
            suggestions = {"zh_cn", "en_us"}
    )
    public static String language = "zh_cn";

    public static class ViewDistanceValidator implements IValidator<Integer> {
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
    public static int viewDistance = 0;

    @Rule(
            categories = CREATIVE
    )
    public static boolean xpNoCooldown = false;

    @Rule(
            categories = COMMAND
    )
    public static boolean allowSpawningOfflinePlayers = false;

    @Rule(
            categories = COMMAND,
            validators = {Validators.CommandLevel.class},
            suggestions = {"ops", "true", "false"}
    )
    public static String commandPlayer = "ops";

    @Rule(
            categories = COMMAND,
            validators = {Validators.CommandLevel.class},
            suggestions = {"ops", "true", "false"}
    )
    public static String commandLog = "true";

    @Rule(
            categories = SURVIVAL
    )
    public static boolean missingTools = false;

    @Rule(
            categories = {CREATIVE, SURVIVAL, FEATURE}
    )
    public static boolean flippingCactus = false;

    @Rule(
            categories = {FEATURE}
    )
    public static boolean rotatorBlock = false;

    @Rule(
            categories = BUGFIX
    )
    public static boolean placementRotationFix = false;

    @Rule(
            categories = {CREATIVE, SURVIVAL},
            suggestions = {"none", "tps", "mobcaps", "mobcaps,tps"}
    )
    public static String defaultLoggers = "none";

    @Rule(
            categories = {SURVIVAL},
            suggestions = {"1", "5", "20", "100"},
            serializedName = "hud_logger_update_interval"
    )
    public static int HUDLoggerUpdateInterval = 20;

    @Rule(
            categories = CREATIVE,
            suggestions = {"none"},
            serializedName = "custom_motd"
    )
    public static String customMOTD = "none";

    public static class StackableShulkerBoxValidator implements IValidator<String> {

        @Override
        public boolean validate(CommandSourceStack source, CurtainRule<String> rule, String newValue) {
            if (newValue.matches("^[0-9]+$")) {
                int value = Integer.parseInt(newValue);
                if (value <= 64 && value >= 2) {
                    shulkerBoxStackSize = value;
                    return true;
                }
            }
            if (newValue.equalsIgnoreCase("false")) {
                shulkerBoxStackSize = 1;
                return true;
            }
            if (newValue.equalsIgnoreCase("true")) {
                shulkerBoxStackSize = 64;
                return true;
            }
            return false;
        }
    }

    @Rule(
            categories = {SURVIVAL, FEATURE},
            suggestions = {"false", "true", "16"},
            validators = StackableShulkerBoxValidator.class
    )
    public static String stackableShulkerBoxes = "false";
    public static int shulkerBoxStackSize = 1;

    @Rule(
            categories = {CREATIVE, CLIENT}
    )
    public static boolean creativeNoClip = false;

    public static boolean isCreativeFlying(Entity entity) {
        return creativeNoClip && entity instanceof Player && (((Player) entity).isCreative()) && ((Player) entity).getAbilities().flying;
    }

    public static class FakePlayerNameValidator implements IValidator<String> {
        @Override
        public boolean validate(CommandSourceStack source, CurtainRule<String> rule, String newValue) {
            return newValue.matches("^\\w*$");
        }
    }

    @Rule(
            categories = {COMMAND, BOT},
            suggestions = {"none", "bot_"},
            validators = FakePlayerNameValidator.class
    )
    public static String fakePlayerNamePrefix = "none";

    @Rule(
            categories = {COMMAND, BOT},
            suggestions = {"none", "_fake"},
            validators = FakePlayerNameValidator.class
    )
    public static String fakePlayerNameSuffix = "none";

    @Rule(
            categories = SURVIVAL
    )
    public static boolean quickLeafDecay = false;

    @Rule(
            categories = {FEATURE, CLIENT}
    )
    public static boolean superLead = false;

    @Rule(
            categories = FEATURE
    )
    public static boolean desertShrubs = false;

    @Rule(
            categories = CREATIVE
    )
    public static boolean turtleEggTrampledDisabled = false;

    @Rule(
            categories = CREATIVE
    )
    public static boolean farmlandTrampledDisabled = false;


    @Rule(
            categories = {CREATIVE, TNT}
    )
    public static boolean explosionNoBlockDamage = false;

    @Rule(
            categories = TNT,
            serializedName = "optimized_tnt"
    )
    public static boolean optimizedTNT = false;

    @Rule(
            categories = {SURVIVAL, FEATURE}
    )
    public static boolean xpFromExplosions = false;

    public static class CheckOptimizedTntEnabledValidator implements IValidator<Integer> {

        @Override
        public boolean validate(CommandSourceStack source, CurtainRule<Integer> rule, String newValue) {
            boolean b = optimizedTNT || rule.isDefault(newValue);
            if (!b) {
                source.sendFailure(Component.literal("optimizedTNT must be enabled"));
            }
            return b;
        }
    }

    public static class TNTRandomRangeValidator implements IValidator<Integer> {

        @Override
        public boolean validate(CommandSourceStack source, CurtainRule<Integer> rule, String newValue) {
            double value = Double.parseDouble(newValue);
            return value == -1 || value >= 0;
        }
    }

    @Rule(
            categories = TNT,
            suggestions = {"-1"},
            validators = {CheckOptimizedTntEnabledValidator.class, TNTRandomRangeValidator.class}
    )
    public static double tntRandomRange = -1;

    @Rule(
            categories = {TNT, CREATIVE}
    )
    public static boolean tntPrimerMomentumRemoved = false;

    public static class TNTAngleValidator implements IValidator<Double> {

        @Override
        public boolean validate(CommandSourceStack source, CurtainRule<Double> rule, String newValue) {
            double value = Double.parseDouble(newValue);
            boolean b = ((value >= 0 && value < Math.PI * 2) || rule.isDefault(newValue));
            if (!b) {
                source.sendFailure(Component.literal("Must be between 0 and 2pi, or -1"));
            }
            return b;
        }
    }

    @Rule(
            categories = TNT,
            suggestions = {"0"},
            validators = TNTAngleValidator.class,
            serializedName = "hardcode_tnt_angle"
    )
    public static double hardcodeTNTAngle = -1.0D;
    @Rule(
            categories = TNT,
            serializedName = "merge_tnt"
    )
    public static boolean mergeTNT = false;

    @Rule(
            categories = {BUGFIX, SURVIVAL}
    )
    public static boolean ctrlQCraftingFix = false;


    @Rule(
            categories = {CREATIVE, SURVIVAL, BOT}
    )
    public static boolean openFakePlayerInventory = false;

    @Rule(
            categories = {CREATIVE, SURVIVAL, BOT}
    )
    public static boolean openFakePlayerEnderChest = false;

    public static class ScaffoldingDistanceValidator implements IValidator<Integer> {

        @Override
        public boolean validate(CommandSourceStack source, CurtainRule<Integer> rule, String newValue) {
            int value = Integer.parseInt(newValue);
            return value >= 0 && value <= 7;
        }
    }

    @Rule(
            validators = ScaffoldingDistanceValidator.class,
            categories = {CREATIVE},
            suggestions = {"2", "3", "5", "7"}
    )
    public static int scaffoldingDistance = 7;

    @Rule(
            categories = {CREATIVE, BOT}
    )
    public static boolean fakePlayerAutoFish;

    @Rule(
            categories = {SURVIVAL}
    )
    public static boolean betterSignInteraction = false;

    @Rule(
            categories = {SURVIVAL}
    )
    public static boolean betterWoodStrip = false;

    @Rule(
            categories = {SURVIVAL}
    )
    public static boolean betterFenceGatePlacement = false;

    @Rule(
            categories = {CREATIVE, SURVIVAL, BOT}
    )
    public static boolean fakePlayerResident = false;

    @Rule(
            categories = {CREATIVE, SURVIVAL, BOT}
    )
    public static boolean fakePlayerAutoReplenishment = false;

    @Rule(
            categories = {CREATIVE, SURVIVAL, BOT}
    )
    public static boolean fakePlayerAutoReplaceTool = false;

    @Rule(
            categories = {CREATIVE, SURVIVAL}
    )
    public static boolean blockPlacementIgnoreEntity = false;

    @Rule(
            categories = FEATURE
    )
    public static boolean emptyShulkerBoxStackAlways = false;

    @Rule(
            categories = {FEATURE}
    )
    public static boolean chickenShearing = false;

    @Rule(
            categories = {CREATIVE, SURVIVAL}
    )
    public static boolean antiCheatDisabled = false;
}
