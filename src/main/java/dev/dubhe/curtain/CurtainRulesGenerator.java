//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dev.dubhe.curtain;

import dev.dubhe.curtain.api.rules.CurtainRule;
import dev.dubhe.curtain.api.rules.RuleManager;
import dev.dubhe.curtain.utils.TranslationHelper;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

public class CurtainRulesGenerator implements ICurtain {
    public CurtainRulesGenerator() {
    }

    public static void main(String[] args) {
        RuleManager.addRules(CurtainRules.class);
        setTrans();
        StringBuilder sb = new StringBuilder("* Add Features\n");
        Iterator var2 = RuleManager.RULES.entrySet().iterator();

        while (var2.hasNext()) {
            Map.Entry<String, CurtainRule<?>> entry = (Map.Entry) var2.next();
            CurtainRule<?> rule = entry.getValue();
            sb.append("    * ").append((String) entry.getKey()).append("\n");
            sb.append("        * 名称：`").append(TranslationHelper.translate(rule.getNameTranslationKey(), new Object[0]).getString()).append("`\n");
            sb.append("        * 描述：`").append(TranslationHelper.translate(rule.getDescTranslationKey(), new Object[0]).getString()).append("`\n");
            sb.append("        * 类型：`").append(getTypeString(rule.getType())).append("`\n");
            sb.append("        * 默认：`").append(rule.getDefaultValue()).append("`\n");
            String[] suggestions = rule.getSuggestions();
            StringBuilder suggestion = new StringBuilder();
            String[] var7 = suggestions;
            int var8 = suggestions.length;

            for (int var9 = 0; var9 < var8; ++var9) {
                String s = var7[var9];
                suggestion.append("`").append(s).append("`").append(", ");
            }

            String str = suggestion.toString();
            sb.append("        * 建议：").append(str, 0, str.length() - 2).append("\n");
            String[] categories = rule.getCategories();
            StringBuilder category = new StringBuilder();
            String[] var21 = categories;
            int var11 = categories.length;

            for (int var12 = 0; var12 < var11; ++var12) {
                String s = var21[var12];
                category.append("`").append(TranslationHelper.translate("%s.categories.%s".formatted("curtain", s), new Object[0]).getString()).append("`").append(", ");
            }

            str = category.toString();
            sb.append("        * 分类：").append(str, 0, str.length() - 2).append("\n");
        }

        try {
            FileWriter fileWriter = new FileWriter("V.MD");

            try {
                fileWriter.write("");
                fileWriter.write(sb.toString());
                fileWriter.flush();
            } catch (Throwable var15) {
                try {
                    fileWriter.close();
                } catch (Throwable var14) {
                    var15.addSuppressed(var14);
                }

                throw var15;
            }

            fileWriter.close();
        } catch (IOException var16) {
            var16.printStackTrace();
        }

    }

    private static void setTrans() {
        InputStream stream = TranslationHelper.class.getClassLoader().getResourceAsStream("assets/curtain/lang/zh_cn.json");
        TranslationHelper.addTransMap("zh_cn", TranslationHelper.getTranslationFromResourcePath(stream));
        stream = TranslationHelper.class.getClassLoader().getResourceAsStream("assets/curtain/lang/en_us.json");
        TranslationHelper.addTransMap("en_us", TranslationHelper.getTranslationFromResourcePath(stream));
    }

    private static String getTypeString(Class<?> clazz) {
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
