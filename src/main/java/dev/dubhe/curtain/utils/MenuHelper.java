package dev.dubhe.curtain.utils;

import dev.dubhe.curtain.Curtain;
import dev.dubhe.curtain.api.rules.CurtainRule;
import dev.dubhe.curtain.api.rules.RuleManager;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.fml.ModList;

import java.util.ArrayList;
import java.util.Collection;

import static dev.dubhe.curtain.utils.TranslationKeys.*;

public class MenuHelper {
    /**
     * 主菜单
     *
     * @return 主菜单聊天组件
     */
    public static TextComponent main() {
        TextComponent main = new StringTextComponent("");
        IFormattableTextComponent title = TranslationHelper.translate(MENU_TITLE).withStyle(s -> s.withBold(true));
        main.append(title).append("\n");
        for (CurtainRule<?> rule : Curtain.rules.ruleMap.values()) {
            main.append(rule(rule)).append("\n");
        }
        String v = ModList.get().getModContainerById(Curtain.MODID).get().getModInfo().getVersion().toString();
        IFormattableTextComponent version = TranslationHelper.translate(MENU_VERSION, v).withStyle(s -> s.withColor(TextFormatting.GRAY));
        main.append(version).append("\n");
        IFormattableTextComponent categories = TranslationHelper.translate(MENU_CATEGORIES).withStyle(s -> s.withBold(true));
        main.append(categories);
        for (String s : RuleManager.CATEGORIES_RULES.keySet()) {
            IFormattableTextComponent category = new StringTextComponent("").withStyle(style -> style.withColor(TextFormatting.AQUA));
            category.append("[");
            category.append(TranslationHelper.translate(String.format(CATEGORIES, Curtain.MODID, s)));
            category.append("]");
            category.append(" ");
            category.withStyle(style -> style.withClickEvent(
                    new ClickEvent(
                            ClickEvent.Action.RUN_COMMAND,
                            "/curtain category " + s
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
    public static IFormattableTextComponent category(String name) {
        IFormattableTextComponent main = new StringTextComponent("");
        IFormattableTextComponent display = new StringTextComponent(name);
        if (RuleManager.CATEGORIES_RULES.containsKey(name)) {
            display = TranslationHelper.translate(String.format(CATEGORIES, Curtain.MODID, name));
        }
        main.append(TranslationHelper.translate(MENU_CATEGORY, display.getString()).withStyle(style -> style.withBold(true)));
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
    public static TextComponent rule(CurtainRule<?> rule) {
        TextComponent main = new StringTextComponent("");
        TextComponent name = new StringTextComponent("");
        name.append(rule.getNameComponent());
        name.append(new StringTextComponent(String.format("(%s): ", rule.getNormalName())));
        name.withStyle(
                style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, rule.getDescComponent()))
        );
        String value = String.valueOf(rule.getValue());
        Collection<String> suggestion = rule.getExamples();
        main.append(name);
        IFormattableTextComponent v = new StringTextComponent(String.format("[%s]", value))
                .withStyle(style -> style
                        .withColor(rule.isDefault(value) ? TextFormatting.DARK_GREEN : TextFormatting.YELLOW)
                        .withUnderlined(true)
                        .withClickEvent(new ClickEvent(
                                ClickEvent.Action.SUGGEST_COMMAND,
                                String.format("/curtain setValue %s %s", rule.getNormalName(), value)
                        ))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent("单击来快速填充"))));
        main.append(v);
        for (String s : suggestion) {
            if (s.equals(value)) continue;
            main.append(" ");
            IFormattableTextComponent x = new StringTextComponent(String.format("[%s]", replaceQuotation(s)))
                    .withStyle(style -> style
                            .withColor(rule.isDefault(replaceQuotation(s)) ? TextFormatting.DARK_GREEN : TextFormatting.YELLOW)
                            .withClickEvent(new ClickEvent(
                                    ClickEvent.Action.SUGGEST_COMMAND,
                                    String.format("/curtain setValue %s %s", rule.getNormalName(), s)
                            ))
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent("单击来快速填充"))));
            main.append(x);
        }
        return main;
    }

    private static String replaceQuotation(String s) {
        return s.replace("\"", "");
    }
}
