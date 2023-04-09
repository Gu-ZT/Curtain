package dev.dubhe.curtain;

import dev.dubhe.curtain.api.rules.RuleManager;
import dev.dubhe.curtain.utils.TranslationHelper;

import java.io.InputStream;

public interface ICurtain {

    /**
     * 解析翻译文件
     *
     * @param name   语言名
     * @param stream 输入流
     */
    default void parseTrans(String name, InputStream stream) {
        TranslationHelper.addTransMap(name, TranslationHelper.getTranslationFromResourcePath(stream));
    }

    /**
     * 添加规则
     *
     * @param rulesClass 规则类
     */
    default void addRules(Class<?> rulesClass) {
        RuleManager.addRules(rulesClass);
    }
}
