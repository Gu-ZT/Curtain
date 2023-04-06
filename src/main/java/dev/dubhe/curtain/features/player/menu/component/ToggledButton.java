package dev.dubhe.curtain.features.player.menu.component;


import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.IFormattableTextComponent;

import java.util.ArrayList;
import java.util.List;

public class ToggledButton extends Button {

    private boolean IsToggled; // on true / off false
    private final Item ToggledOff_DisplayItem; // off状态展示的物品
    private final IFormattableTextComponent ToggledOff_Text; // off状态展示的文本
    private final List<Function> ToggledOff_Func = new ArrayList<>(); // 关闭时候事件

    public ToggledButton(Item toggledon_DisplayItem, Item toggledOff_DisplayItem, IFormattableTextComponent toggledOn_text, IFormattableTextComponent toggledOff_Text, int itemCount, IInventory container, int slot) {
        this(true, toggledon_DisplayItem, itemCount, toggledOn_text, Items.BARRIER, toggledOff_DisplayItem, toggledOff_Text, container, slot);
    }

    public ToggledButton(boolean isEnable, Item displayItem, int itemCount, IFormattableTextComponent text, Item disabled_DisplayItem, Item toggledOff_DisplayItem, IFormattableTextComponent toggledOff_Text, IInventory container, int slot) {
        super(isEnable, displayItem, itemCount, text, disabled_DisplayItem, container, slot);
        this.ToggledOff_DisplayItem = toggledOff_DisplayItem;
        this.ToggledOff_Text = toggledOff_Text;
    }

    @Override
    public void onClick() {
        if (super.Enabled) {
            if (IsToggled) {
                for (var func :
                        ToggledOff_Func) {
                    func.accept();
                }
            } else {
                for (var func :
                        super.Functions)
                    func.accept();
            }
            IsToggled = !IsToggled;
        }
        setupOrUpdateButton();
    }

    @Override
    protected void setupOrUpdateButton() {
        ItemStack DisplayItem = new ItemStack(
                Enabled ?
                        IsToggled ?
                                this.Enabled_DisplayItem :
                                this.ToggledOff_DisplayItem
                        : this.Disabled_DisplayItem,
                this.ItemCount
        );

        DisplayItem.setTag(compoundTag.copy());
        DisplayItem.setHoverName(
                IsToggled ?
                        this.Text :
                        this.ToggledOff_Text
        );

        ItemStack slotItem = container.getItem(Slot);
        if (slotItem.is(this.Enabled_DisplayItem) || slotItem.is(this.Disabled_DisplayItem) || slotItem.is(this.ToggledOff_DisplayItem) || slotItem.isEmpty()) {
            container.setItem(Slot, DisplayItem);
        }
    }

    public void addToggledOffEvent(Function function) {
        ToggledOff_Func.add(function);
    }

    @Override
    public void reset() {
        this.IsToggled = false;
        super.reset();
    }
}
