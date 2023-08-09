package dev.dubhe.curtain.api.rules;

import com.google.common.base.CaseFormat;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.dubhe.curtain.Curtain;
import dev.dubhe.curtain.utils.TranslationHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static dev.dubhe.curtain.utils.TranslationKeys.RULE_DESC;
import static dev.dubhe.curtain.utils.TranslationKeys.RULE_NAME;

public class CurtainRule<T> implements ArgumentType<T>, CommandExceptionType {
    private final String[] categories;
    private final List<IValidator<T>> validators;
    private final String[] suggestions;
    private final Field field;
    private final T defaultValue;

    private final String nameTranslationKey;
    private final String descTranslationKey;

    private CurtainRule(String[] categories, List<IValidator<T>> validators, String[] suggestions, Field field) {
        this(categories, validators, suggestions, field, CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName()));
    }

    @SuppressWarnings("unchecked")
    private CurtainRule(String[] categories, List<IValidator<T>> validators, String[] suggestions, Field field, String serializedName) {
        this.categories = categories;
        this.validators = validators;
        this.suggestions = field.getType() == boolean.class || field.getType() == Boolean.class ? new String[]{"true", "false"} : suggestions;
        this.field = field;
        nameTranslationKey = RULE_NAME.formatted(Curtain.MODID, serializedName);
        descTranslationKey = RULE_DESC.formatted(Curtain.MODID, serializedName);
        try {
            this.defaultValue = (T) field.get(null);
        } catch (IllegalAccessException e) {
            throw new RuleException(e.getMessage());
        }
    }


    public void setValue(T value) throws IllegalAccessException {
        field.set(field, value);
    }

    public void setValue(Object value, Class<?> typeOfT) {
        try {
            field.set(field, value);
        } catch (IllegalAccessException e) {
            throw new RuleException(e.getMessage());
        }
    }

    public boolean validate(CommandSourceStack source, String newValue) {
        for (IValidator<T> validator : validators) {
            if (!validator.validate(source, this, newValue)) return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public static <T> CurtainRule<T> newRule(String[] categories, Class<? extends IValidator<?>> @NotNull [] validators, String[] suggestions, Field field, String serializedName) {
        List<IValidator<T>> validators1 = new ArrayList<>();
        for (Class<? extends IValidator<?>> validator : validators) {
            try {
                validators1.add((IValidator<T>) validator.getDeclaredConstructor().newInstance());
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                     InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return new CurtainRule<>(categories, validators1, suggestions, field, serializedName);
    }

    @SuppressWarnings("unchecked")
    public static <T> CurtainRule<T> newRule(String[] categories, Class<? extends IValidator<?>> @NotNull [] validators, String[] suggestions, Field field) {
        List<IValidator<T>> validators1 = new ArrayList<>();
        for (Class<? extends IValidator<?>> validator : validators) {
            try {
                validators1.add((IValidator<T>) validator.getDeclaredConstructor().newInstance());
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                     InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return new CurtainRule<>(categories, validators1, suggestions, field);
    }

    @Override
    public String toString() {
        try {
            return field.get(null).toString();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return "null";
    }


    @SuppressWarnings("unchecked")
    public T getValue() {
        try {
            return (T) field.get(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void reset() {
        try {
            this.setValue(defaultValue);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public T parse(StringReader reader) throws CommandSyntaxException {
        String str = reader.getString();
        if (this.field.getType() == String.class)
            return (T) str;
        if (this.field.getType() == Boolean.class)
            return (T) (Boolean) Boolean.parseBoolean(str);
        else if (this.field.getType() == Byte.class)
            return (T) (Byte) Byte.parseByte(str);
        else if (this.field.getType() == Short.class)
            return (T) (Short) Short.parseShort(str);
        else if (this.field.getType() == Integer.class)
            return (T) (Integer) Integer.parseInt(str);
        else if (this.field.getType() == Long.class)
            return (T) (Long) Long.parseLong(str);
        else if (this.field.getType() == Float.class)
            return (T) (Float) Float.parseFloat(str);
        else if (this.field.getType() == Double.class)
            return (T) (Double) Double.parseDouble(str);
        else throw new CommandSyntaxException(this, Component.literal("%s is not a legal value".formatted(str)));
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(this.suggestions, builder);
    }

    @Override
    public Collection<String> getExamples() {
        if (this.getType() == String.class) {
            ArrayList<String> rt = new ArrayList<>();
            for (String s : this.suggestions) {
                rt.add("\"%s\"".formatted(s));
            }
            return rt;
        } else {
            return List.of(this.suggestions);
        }
    }

    public String[] getCategories() {
        return categories;
    }

    public MutableComponent getNameComponent() {
        return TranslationHelper.translate(this.getNameTranslationKey());
    }

    public MutableComponent getDescComponent() {
        return TranslationHelper.translate(this.getDescTranslationKey());
    }

    public String getNameTranslationKey() {
        return nameTranslationKey;
    }

    public String getDescTranslationKey() {
        return descTranslationKey;
    }

    public Class<?> getType() {
        return this.field.getType();
    }

    public String getName() {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName());
    }

    public boolean isDefault(String s) {
        return String.valueOf(this.defaultValue).equals(s);
    }

    public String getNormalName() {
        return field.getName();
    }

    public String[] getSuggestions() {
        return suggestions;
    }

    public T getDefaultValue() {
        return defaultValue;
    }
}
