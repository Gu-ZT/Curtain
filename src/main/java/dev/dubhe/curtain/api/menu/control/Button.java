package dev.dubhe.curtain.api.menu.control;

import dev.dubhe.curtain.api.Function;
import dev.dubhe.curtain.utils.TranslationHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

public class Button {

    private boolean init = false;
    private boolean flag;
    private final Item onItem;
    private final Item offItem;
    private final int itemCount;
    private final IFormattableTextComponent onText;
    private final IFormattableTextComponent offText;
    CompoundNBT compoundTag = new CompoundNBT();

    private final List<Function> turnOnFunctions = new ArrayList<>();

    private final List<Function> turnOffFunctions = new ArrayList<>();

    public Button() {
        this(true, Items.BARRIER, Items.STRUCTURE_VOID);
    }

    public Button(boolean defaultState) {
        this(defaultState, Items.BARRIER, Items.STRUCTURE_VOID);
    }

    public Button(boolean defaultState, int itemCount) {
        this(defaultState, Items.BARRIER, Items.STRUCTURE_VOID, itemCount);
    }

    public Button(boolean defaultState, int itemCount, IFormattableTextComponent onText, IFormattableTextComponent offText) {
        this(defaultState, Items.BARRIER, Items.STRUCTURE_VOID, itemCount, onText, offText);
    }

    public Button(boolean defaultState, IFormattableTextComponent onText, IFormattableTextComponent offText) {
        this(defaultState, Items.BARRIER, Items.STRUCTURE_VOID, 1, onText, offText);
    }

    public Button(boolean defaultState, String key) {
        this(defaultState, Items.BARRIER, Items.STRUCTURE_VOID, 1,
                TranslationHelper.translate(key, TextFormatting.GREEN, Style.EMPTY.withBold(true).withItalic(false), "on"),
                TranslationHelper.translate(key, TextFormatting.RED, Style.EMPTY.withBold(true).withItalic(false), "off")
        );
    }

    public Button(boolean defaultState, Item onItem, Item offItem) {
        this(defaultState, onItem, offItem, 1);
    }

    public Button(boolean defaultState, Item onItem, Item offItem, int itemCount) {
        this(defaultState, onItem, offItem, itemCount,
                TranslationHelper.translate("on", TextFormatting.GREEN, Style.EMPTY.withBold(true).withItalic(false)),
                TranslationHelper.translate("off", TextFormatting.RED, Style.EMPTY.withBold(true).withItalic(false))
        );
    }

    public Button(boolean defaultState, Item onItem, Item offItem, int itemCount, IFormattableTextComponent onText, IFormattableTextComponent offText) {
        this.flag = defaultState;
        this.onText = onText;
        this.offText = offText;
        this.onItem = onItem;
        this.offItem = offItem;
        this.itemCount = itemCount;
        this.compoundTag.putBoolean("GcaClear", true);
    }

    public void checkButton(IInventory container, int slot) {
        ItemStack onItemStack = new ItemStack(this.onItem, this.itemCount);
        onItemStack.setTag(compoundTag.copy());
        onItemStack.setHoverName(this.onText);

        ItemStack offItemStack = new ItemStack(this.offItem, this.itemCount);
        offItemStack.setTag(compoundTag.copy());
        offItemStack.setHoverName(this.offText);

        if (!this.init) {
            updateButton(container, slot, onItemStack, offItemStack);
            this.init = true;
        }

        ItemStack item = container.getItem(slot);

        if (item.isEmpty()) {
            this.flag = !flag;
            if (flag) {
                runTurnOnFunction();
            } else {
                runTurnOffFunction();
            }
        }

        updateButton(container, slot, onItemStack, offItemStack);
    }

    public void updateButton(IInventory container, int slot, ItemStack onItemStack, ItemStack offItemStack) {
        if (!(
                container.getItem(slot).getItem() == onItemStack.getItem() ||
                        container.getItem(slot).getItem() == offItemStack.getItem() ||
                        container.getItem(slot).isEmpty()
        )) {
            return;
        }
        if (flag) {
            container.setItem(slot, onItemStack);
        } else {
            container.setItem(slot, offItemStack);
        }
    }

    public void addTurnOnFunction(Function function) {
        this.turnOnFunctions.add(function);
    }

    public void addTurnOffFunction(Function function) {
        this.turnOffFunctions.add(function);
    }

    public void turnOnWithoutFunction() {
        this.flag = true;
    }

    public void turnOffWithoutFunction() {
        this.flag = false;
    }

    public void turnOn() {
        this.flag = true;
        runTurnOnFunction();
    }

    public void turnOff() {
        this.flag = false;
        runTurnOffFunction();
    }

    public void runTurnOnFunction() {
        for (Function turnOnFunction : this.turnOnFunctions) {
            turnOnFunction.accept();
        }
    }

    public void runTurnOffFunction() {
        for (Function turnOffFunction : this.turnOffFunctions) {
            turnOffFunction.accept();
        }
    }

    public boolean getFlag() {
        return flag;
    }
}
