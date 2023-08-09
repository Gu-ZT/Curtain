package dev.dubhe.curtain.api.menu.control;

import dev.dubhe.curtain.utils.TranslationHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;

public class AutoResetButton extends Button {

    public AutoResetButton(String key) {
        super(false,
                TranslationHelper.translate(key, ChatFormatting.WHITE, Style.EMPTY.withBold(true).withItalic(false)),
                TranslationHelper.translate(key, ChatFormatting.WHITE, Style.EMPTY.withBold(true).withItalic(false))
        );
        this.addTurnOnFunction(this::turnOffWithoutFunction);
    }
}
