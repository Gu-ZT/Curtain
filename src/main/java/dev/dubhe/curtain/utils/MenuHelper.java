package dev.dubhe.curtain.utils;

import dev.dubhe.curtain.Curtain;
import dev.dubhe.curtain.api.rules.CurtainRule;
import dev.dubhe.curtain.api.rules.RuleManager;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

import static dev.dubhe.curtain.utils.TranslationKeys.CATEGORIES;
import static dev.dubhe.curtain.utils.TranslationKeys.MENU_CATEGORIES;
import static dev.dubhe.curtain.utils.TranslationKeys.MENU_CATEGORY;
import static dev.dubhe.curtain.utils.TranslationKeys.MENU_TITLE;
import static dev.dubhe.curtain.utils.TranslationKeys.MENU_VERSION;

public class MenuHelper {
    /**
     * 主菜单
     *
     * @return 主菜单聊天组件
     */
    public static @NotNull Component main() {
        MutableComponent main = Component.empty();
        MutableComponent title = TranslationHelper.translate(MENU_TITLE).withStyle(Style.EMPTY.withBold(true));
        main.append(title).append("\n");
        for (CurtainRule<?> rule : Curtain.rules.ruleMap.values()) {
            main.append(rule(rule)).append("\n");
        }
        String v = ModList.get().getModFileById(Curtain.MODID).versionString();
        MutableComponent version = TranslationHelper.translate(MENU_VERSION, v).withStyle(ChatFormatting.GRAY);
        main.append(version).append("\n");
        MutableComponent categories = TranslationHelper.translate(MENU_CATEGORIES).withStyle(Style.EMPTY.withBold(true));
        main.append(categories);
        for (String s : RuleManager.CATEGORIES_RULES.keySet()) {
            MutableComponent category = Component.empty().withStyle(ChatFormatting.AQUA);
            category.append("[");
            category.append(TranslationHelper.translate(CATEGORIES.formatted(Curtain.MODID, s)));
            category.append("]");
            category.append(" ");
            category.withStyle(Style.EMPTY.withClickEvent(
                    new ClickEvent(
                            ClickEvent.Action.RUN_COMMAND,
                            "/curtain category %s".formatted(s)
                    )
            ));
            main.append(category);
        }
        return main;
    }

    /**
     * 分类
     *
     * @param name 分类名
     * @return 分类聊天组件
     */
    public static @NotNull Component category(String name) {
        MutableComponent main = Component.empty();
        MutableComponent display = Component.literal(name);
        if (RuleManager.CATEGORIES_RULES.containsKey(name)) {
            display = TranslationHelper.translate(CATEGORIES.formatted(Curtain.MODID, name));
        }
        main.append(TranslationHelper.translate(MENU_CATEGORY, display.getString()).withStyle(Style.EMPTY.withBold(true)));
        for (String rule : RuleManager.CATEGORIES_RULES.getOrDefault(name, new ArrayList<>())) {
            main.append("\n").append(rule(RuleManager.RULES.get(rule)));
        }
        return main;
    }

    /**
     * 规则
     *
     * @param rule 规则
     * @return 规则聊天组件
     */
    public static @NotNull Component rule(@NotNull CurtainRule<?> rule) {
        MutableComponent main = Component.empty();
        MutableComponent name = Component.empty();
        name.append(rule.getNameComponent());
        name.append(Component.literal("(%s): ".formatted(rule.getNormalName())));
        name.withStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, rule.getDescComponent())));
        String value = String.valueOf(rule.getValue());
        Collection<String> suggestion = rule.getExamples();
        main.append(name);
        MutableComponent v = Component.literal("[%s]".formatted(value))
                .withStyle(rule.isDefault(value) ? ChatFormatting.DARK_GREEN : ChatFormatting.YELLOW)
                .withStyle(Style.EMPTY
                        .withUnderlined(true)
                        .withClickEvent(new ClickEvent(
                                ClickEvent.Action.SUGGEST_COMMAND,
                                "/curtain setValue %s %s".formatted(rule.getNormalName(),
                                        rule.getType() == String.class ? "\"%s\"".formatted(value) : value)
                        ))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("单击来快速填充"))));
        main.append(v);
        for (String s : suggestion) {
            if (replaceQuotation(s).equals(value)) continue;
            main.append(" ");
            MutableComponent x = Component.literal("[%s]".formatted(replaceQuotation(s)))
                    .withStyle(rule.isDefault(replaceQuotation(s)) ? ChatFormatting.DARK_GREEN : ChatFormatting.YELLOW);
            x.withStyle(Style.EMPTY.withClickEvent(new ClickEvent(
                    ClickEvent.Action.SUGGEST_COMMAND,
                    "/curtain setValue %s %s".formatted(rule.getNormalName(), s)
            )).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("单击来快速填充"))));
            main.append(x);
        }
        return main;
    }

    private static String replaceQuotation(String s) {
        return s.replace("\"", "");
    }
}
