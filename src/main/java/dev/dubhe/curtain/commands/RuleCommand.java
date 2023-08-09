package dev.dubhe.curtain.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.dubhe.curtain.Curtain;
import dev.dubhe.curtain.api.rules.Categories;
import dev.dubhe.curtain.api.rules.CurtainRule;
import dev.dubhe.curtain.api.rules.RuleException;
import dev.dubhe.curtain.api.rules.RuleManager;
import dev.dubhe.curtain.utils.MenuHelper;
import dev.dubhe.curtain.utils.TranslationHelper;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;

import static dev.dubhe.curtain.utils.TranslationKeys.*;
import static net.minecraft.command.ISuggestionProvider.suggest;

public class RuleCommand {
    public static void register(CommandDispatcher<CommandSource> dispatcher, RuleManager manager) {
        dispatcher.register(Commands.literal(manager.getId()).requires(stack -> stack.hasPermission(2))
                .executes(RuleCommand::showMenu)
                .then(Commands.literal("category")
                        .then(Commands.argument("name", StringArgumentType.word())
                                .suggests((context, builder) -> suggest(Categories.getCategories(), builder))
                                .executes(RuleCommand::showCategory))
                )
                .then(RuleCommand.valueNode(false, Commands.literal("setValue")))
                .then(RuleCommand.valueNode(true, Commands.literal("setDefault")))
        );
    }

    private static int showMenu(CommandContext<CommandSource> context) {
        TextComponent component = MenuHelper.main();
        context.getSource().sendSuccess(component, false);
        return 1;
    }

    private static int showCategory(CommandContext<CommandSource> context) {
        IFormattableTextComponent component = MenuHelper.category(context.getArgument("name", String.class));
        context.getSource().sendSuccess(component, false);
        return 1;
    }

    private static ArgumentBuilder<CommandSource, ?> valueNode(boolean setDefault, ArgumentBuilder<CommandSource, ?> builder) {
        for (CurtainRule<?> rule : RuleManager.RULES.values()) {
            builder.then(Commands.literal(rule.getNormalName()).executes(context -> RuleCommand.getValue(context, rule))
                    .then(
                            Commands.argument("value", RuleCommand.getValue(rule.getType()))
                                    .suggests((c, b) -> suggest(rule.getExamples(), b))
                                    .executes(context -> setValue(rule.getNormalName(), context, setDefault))
                    ));
        }
        return builder;
    }

    private static ArgumentType<?> getValue(Class<?> type) {
        if (type == String.class) {
            return StringArgumentType.string();
        } else if (type == Boolean.class || type == boolean.class) {
            return BoolArgumentType.bool();
        } else if (type == Byte.class || type == byte.class) {
            return IntegerArgumentType.integer();
        } else if (type == Short.class || type == short.class) {
            return IntegerArgumentType.integer();
        } else if (type == Integer.class || type == int.class) {
            return IntegerArgumentType.integer();
        } else if (type == Long.class || type == long.class) {
            return IntegerArgumentType.integer();
        } else if (type == Float.class || type == float.class) {
            return FloatArgumentType.floatArg();
        } else if (type == Double.class || type == double.class) {
            return DoubleArgumentType.doubleArg();
        } else throw RuleException.type();
    }

    private static int getValue(CommandContext<CommandSource> context, CurtainRule<?> rule) {
        context.getSource().sendSuccess(MenuHelper.rule(rule), false);
        return 1;
    }

    private static int setValue(String name, CommandContext<CommandSource> context, boolean setDefault) {
        CurtainRule<?> rule = RuleManager.RULES.get(name);
        if (null == rule) throw RuleException.nu11();
        Object obj = context.getArgument("value", rule.getType());
        if (!rule.validate(context.getSource(), String.valueOf(obj))) throw RuleException.legal();
        rule.setValue(obj, rule.getType());
        String ruleName = rule.getNameComponent().getString();
        if (setDefault) {
            Curtain.rules.setDefault(rule.getNormalName());
            Curtain.rules.saveToFile();
            context.getSource().sendSuccess(
                    TranslationHelper.translate(CHANGE_DEFAULT, ruleName, obj).withStyle(TextFormatting.GRAY),
                    false
            );
        } else {
            IFormattableTextComponent component = TranslationHelper.translate(CHANGE, ruleName, obj)
                    .withStyle(s -> s.withColor(TextFormatting.GRAY))
                    .append(" ")
                    .append(
                            TranslationHelper.translate(AS_DEFAULT)
                                    .withStyle(s -> s
                                            .withColor(TextFormatting.DARK_GREEN)
                                            .withClickEvent(new ClickEvent(
                                                    ClickEvent.Action.SUGGEST_COMMAND,
                                                    String.format("/curtain setDefault %s %s", name, obj)
                                            )))
                    );
            context.getSource().sendSuccess(component, false);
            return 0;
        }
        return 1;
    }
}
