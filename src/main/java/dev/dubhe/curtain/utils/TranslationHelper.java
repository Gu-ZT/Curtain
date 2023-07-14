package dev.dubhe.curtain.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dev.dubhe.curtain.CurtainRules;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TranslationHelper {
    private static final Gson GSON = new GsonBuilder().setLenient().create();
    private static final Map<String, Map<String, String>> TRANS_MAP = new HashMap<>();

    static {
        TRANS_MAP.put("zh_cn", new HashMap<>());
    }

    /**
     * 获取翻译后的文本
     *
     * @param key  翻译键
     * @param args 参数
     * @return 翻译后的聊天组件
     */
    public static @NotNull MutableComponent translate(String key, ChatFormatting formatting, Style style, Object... args) {
        Map<String, String> trans = TRANS_MAP.getOrDefault(CurtainRules.language, new HashMap<>());
        return Component.literal(trans.getOrDefault(key, key).formatted(args));
    }

    /**
     * 获取翻译后的文本
     *
     * @param key  翻译键
     * @param args 参数
     * @return 翻译后的聊天组件
     */
    public static @NotNull MutableComponent translate(String key, Object... args) {
        Map<String, String> trans = TRANS_MAP.getOrDefault(CurtainRules.language, new HashMap<>());
        return Component.translatableWithFallback(key, trans.getOrDefault(key, key).formatted(args), args);
    }

    /**
     * 获取已经加载的语言
     *
     * @return 已经加载的语言名称集合
     */
    public static @NotNull Collection<String> getLanguages() {
        return TRANS_MAP.keySet();
    }

    /**
     * 添加翻译映射表
     *
     * @param lang     语言
     * @param transMap 翻译映射表
     */
    public static void addTransMap(String lang, Map<String, String> transMap) {
        if (!TRANS_MAP.containsKey(lang)) TRANS_MAP.put(lang, transMap);
        else TRANS_MAP.get(lang).putAll(transMap);
    }

    /**
     * 从资源获取翻译内容
     *
     * @param stream 输入流
     * @return 翻译键值映射
     */
    public static Map<String, String> getTranslationFromResourcePath(InputStream stream) {
        String dataJSON;
        try {
            dataJSON = IOUtils.toString(
                    Objects.requireNonNull(stream),
                    StandardCharsets.UTF_8);
            stream.close();
        } catch (NullPointerException | IOException e) {
            return Map.of();
        }
        return GSON.fromJson(dataJSON, new TypeToken<Map<String, String>>() {
        }.getType());
    }
}
