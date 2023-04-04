package dev.dubhe.curtain.api.rules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuleManager {
    public static final List<Class<?>> LIMIT = new ArrayList<>();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final Map<String, CurtainRule<?>> RULES = new HashMap<>();
    public static final Map<String, List<String>> CATEGORIES_RULES = new HashMap<>();
    public final Map<String, CurtainRule<?>> ruleMap = new HashMap<>();
    public final Map<String, CurtainRule<?>> defaultRuleMap = new HashMap<>();
    private final MinecraftServer server;
    private final String id;

    static {
        Collections.addAll(LIMIT,
                String.class,
                Boolean.class,
                Byte.class,
                Short.class,
                Integer.class,
                Long.class,
                Float.class,
                Double.class,
                boolean.class,
                byte.class,
                short.class,
                int.class,
                long.class,
                float.class,
                double.class
        );
    }

    public RuleManager(MinecraftServer server, String id) {
        this.server = server;
        this.id = id;
        RULES.forEach((s, r) -> r.reset());
        this.loadFromFile();
    }

    public static void addRules(@NotNull Class<?> rules) {
        for (Field field : rules.getFields()) {
            if (!LIMIT.contains(field.getType())) continue;
            Rule annotation = field.getAnnotation(Rule.class);
            if (null == annotation) continue;
            String[] categories = annotation.categories();
            Class<? extends IValidator<?>>[] validators = annotation.validators();
            String[] suggestions = annotation.suggestions();
            CurtainRule<?> rule;
            if (annotation.serializedName().contentEquals("")) {
                rule = CurtainRule.newRule(categories, validators, suggestions, field);
            } else {
                rule = CurtainRule.newRule(categories, validators, suggestions, field, annotation.serializedName());
            }
            String name = rule.getNormalName();
            for (String category : categories) {
                if (!CATEGORIES_RULES.containsKey(category)) CATEGORIES_RULES.put(category, new ArrayList<>());
                CATEGORIES_RULES.get(category).add(name);
            }
            RULES.put(name, rule);
        }
    }

    private @NotNull File getFile() {
        return server.getWorldPath(LevelResource.ROOT).resolve(id + ".json").toFile();
    }

    public void saveToFile() {
        JsonObject object = new JsonObject();
        for (String name : defaultRuleMap.keySet()) {
            CurtainRule<?> rule = defaultRuleMap.get(name);
            Object value = rule.getValue();
            if (value instanceof String str)
                object.addProperty(name, str);
            else if (value instanceof Boolean bool)
                object.addProperty(name, bool);
            else if (value instanceof Byte num)
                object.addProperty(name, num);
            else if (value instanceof Short num)
                object.addProperty(name, num);
            else if (value instanceof Integer num)
                object.addProperty(name, num);
            else if (value instanceof Long num)
                object.addProperty(name, num);
            else if (value instanceof Float num)
                object.addProperty(name, num);
            else if (value instanceof Double num)
                object.addProperty(name, num);
            else
                throw RuleException.type();
        }
        try (FileWriter writer = new FileWriter(this.getFile())) {
            GSON.toJson(object, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFromFile() {
        if (!this.getFile().exists()) {
            return;
        }
        try (FileReader reader = new FileReader(this.getFile())) {
            JsonObject object = GSON.fromJson(reader, JsonObject.class);
            for (String name : RULES.keySet()) {
                if (!object.has(name)) continue;
                JsonElement element = object.get(name);
                CurtainRule<?> rule = RULES.get(name);
                if (null == rule) throw RuleException.nu11();
                Object value = rule.getValue();
                if (value instanceof String)
                    this.setValue(rule, element.getAsString());
                else if (value instanceof Boolean)
                    this.setValue(rule, element.getAsBoolean());
                else if (value instanceof Byte)
                    this.setValue(rule, element.getAsByte());
                else if (value instanceof Short)
                    this.setValue(rule, element.getAsShort());
                else if (value instanceof Integer)
                    this.setValue(rule, element.getAsInt());
                else if (value instanceof Long)
                    this.setValue(rule, element.getAsLong());
                else if (value instanceof Float)
                    this.setValue(rule, element.getAsFloat());
                else if (value instanceof Double)
                    this.setValue(rule, element.getAsDouble());
                else
                    throw RuleException.type();
                ruleMap.put(name, rule);
                defaultRuleMap.put(name, rule);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setValue(CurtainRule<?> rule, Object newValue) {
        if (rule.validate(this.server.createCommandSourceStack(), String.valueOf(newValue)))
            rule.setValue(newValue, newValue.getClass());
    }

    public String getId() {
        return this.id;
    }

    public void setDefault(String name) {
        this.defaultRuleMap.put(name, RuleManager.RULES.get(name));
    }
}
