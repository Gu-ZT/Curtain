package dev.dubhe.curtain.api.menu.control;

import dev.dubhe.curtain.utils.TranslationHelper;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

public class AutoResetButton extends Button {

    public AutoResetButton(String key) {
        super(false,
                TranslationHelper.translate(key, TextFormatting.WHITE, Style.EMPTY.withBold(true).withItalic(false)),
                TranslationHelper.translate(key, TextFormatting.WHITE, Style.EMPTY.withBold(true).withItalic(false))
        );
        this.addTurnOnFunction(this::turnOffWithoutFunction);
    }
}
