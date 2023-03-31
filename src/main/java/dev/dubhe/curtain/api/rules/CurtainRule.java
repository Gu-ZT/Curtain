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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static dev.dubhe.curtain.utils.TranslationKeys.*;

public class CurtainRule<T> implements ArgumentType<T>, CommandExceptionType {
    private final String[] categories;
    private final List<Validator<T>> validators;
    private final String[] suggestions;
    private final Field field;
    private final T defaultValue;


    @SuppressWarnings("unchecked")
    private CurtainRule(String[] categories, List<Validator<T>> validators, String[] suggestions, Field field) {
        this.categories = categories;
        this.validators = validators;
        this.suggestions = suggestions;
        this.field = field;
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
        for (Validator<T> validator : validators) {
            if (!validator.validate(source, this, newValue)) return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public static <T> CurtainRule<T> newRule(String[] categories, Class<? extends Validator<?>> @NotNull [] validators, String[] suggestions, Field field) {
        List<Validator<T>> validators1 = new ArrayList<>();
        for (Class<? extends Validator<?>> validator : validators) {
            try {
                validators1.add((Validator<T>)validator.getDeclaredConstructor().newInstance());
            }catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e){
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
        return List.of(this.suggestions);
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
        return RULE_NAME.formatted(Curtain.MODID, this.getName());
    }

    public String getDescTranslationKey() {
        return RULE_DESC.formatted(Curtain.MODID, this.getName());
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
}
