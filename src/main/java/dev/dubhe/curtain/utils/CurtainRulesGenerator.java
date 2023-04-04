package dev.dubhe.curtain.utils;

import dev.dubhe.curtain.CurtainRules;
import dev.dubhe.curtain.api.rules.CurtainRule;
import dev.dubhe.curtain.api.rules.RuleManager;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class CurtainRulesGenerator {
    public CurtainRulesGenerator() {
    }

    public static void main(String[] args) {
        RuleManager.addRules(CurtainRules.class);
        setTrans();
        StringBuilder sb = new StringBuilder("* Add Features\n");

        for (Map.Entry<String, CurtainRule<?>> entry : RuleManager.RULES.entrySet()) {
            CurtainRule<?> rule = entry.getValue();
            sb.append("    * ").append(entry.getKey()).append("\n");
            sb.append("        * 名称：`").append(TranslationHelper.translate(rule.getNameTranslationKey()).getString()).append("`\n");
            sb.append("        * 描述：`").append(TranslationHelper.translate(rule.getDescTranslationKey()).getString()).append("`\n");
            sb.append("        * 类型：`").append(getTypeString(rule.getType())).append("`\n");
            sb.append("        * 默认：`").append(rule.getDefaultValue()).append("`\n");
            String[] suggestions = rule.getSuggestions();
            StringBuilder suggestion = new StringBuilder();
            for (String s : suggestions) {
                suggestion.append("`").append(s).append("`").append(", ");
            }
            String str = suggestion.toString();
            sb.append("        * 建议：").append(str, 0, str.length() - 2).append("\n");
            String[] categories = rule.getCategories();
            StringBuilder category = new StringBuilder();
            for (String s : categories) {
                category.append("`").append(TranslationHelper.translate("%s.categories.%s".formatted("curtain", s)).getString()).append("`").append(", ");
            }
            str = category.toString();
            sb.append("        * 分类：").append(str, 0, str.length() - 2).append("\n");
        }
        try (FileWriter fileWriter = new FileWriter("RULES.MD")) {
            fileWriter.write("");
            fileWriter.write(sb.toString());
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void setTrans() {
        InputStream stream = TranslationHelper.class.getClassLoader().getResourceAsStream("assets/curtain/lang/zh_cn.json");
        TranslationHelper.addTransMap("zh_cn", TranslationHelper.getTranslationFromResourcePath(stream));
        stream = TranslationHelper.class.getClassLoader().getResourceAsStream("assets/curtain/lang/en_us.json");
        TranslationHelper.addTransMap("en_us", TranslationHelper.getTranslationFromResourcePath(stream));
    }

    @Contract(pure = true)
    private static @Nullable String getTypeString(Class<?> clazz) {
        if (clazz != Boolean.TYPE && clazz != Boolean.class) {
            if (clazz != Integer.TYPE && clazz != Integer.class) {
                if (clazz == String.class) {
                    return "String";
                } else {
                    return clazz != Double.TYPE && clazz != Double.class ? null : "Double";
                }
            } else {
                return "Integer";
            }
        } else {
            return "Boolean";
        }
    }
}
