package dev.dubhe.curtain.features.player.menu.component;


import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.IFormattableTextComponent;

import java.util.ArrayList;
import java.util.List;

public class Button {
    protected boolean Enabled; // 是否启用
    protected final Item Enabled_DisplayItem; // 按钮启用展示的物品
    protected final Item Disabled_DisplayItem; // 按钮禁用时的物品
    protected final int ItemCount; // 展示物品数量
    protected final IFormattableTextComponent Text; // 展示的文本
    protected final List<Function> Functions = new ArrayList<>(); // 点击事件

    protected final IInventory container; // 注冊的界面
    protected final int Slot; // 注冊界面的槽位

    protected boolean Init = false; // 是否完成加载
    CompoundNBT compoundTag = new CompoundNBT();


    public Button(IFormattableTextComponent text, IInventory container, int slot) {
        this(true, Items.COMMAND_BLOCK, 1, text, container, slot);
    }

    public Button(boolean isEnable, Item enabled_DisplayItem, int itemCount, IFormattableTextComponent text, IInventory container, int slot) {
        this(isEnable, enabled_DisplayItem, itemCount, text, Items.BARRIER, container, slot);
    }

    public Button(boolean isEnable, Item displayItem, int itemCount, IFormattableTextComponent text, Item disabled_DisplayItem, IInventory container, int slot) {
        this.Enabled = isEnable;
        this.Enabled_DisplayItem = displayItem;
        this.Disabled_DisplayItem = disabled_DisplayItem;
        this.ItemCount = itemCount;
        this.Text = text;
        this.container = container;
        this.Slot = slot;
        this.compoundTag.putBoolean("CurtainGUIItem", true);
    }

    protected void onClick() {
        if (Enabled) {
            for (Function func :
                    this.Functions) {
                func.accept();
            }
        }
        setupOrUpdateButton();
    }

    public void addClickEvent(Function func) {
        this.Functions.add(func);
    }

    public void setEnabled(boolean enabled) {
        this.Enabled = enabled;
        setupOrUpdateButton();
    }

    protected void setupOrUpdateButton() {
        ItemStack DisplayItem = new ItemStack(
                Enabled ? this.Enabled_DisplayItem : this.Disabled_DisplayItem,
                this.ItemCount
        );
        DisplayItem.setTag(compoundTag.copy());
        DisplayItem.setHoverName(this.Text);

        ItemStack slotItem = container.getItem(Slot);
        if (slotItem.getItem() == this.Enabled_DisplayItem || slotItem.getItem() == this.Disabled_DisplayItem || slotItem.isEmpty()) {
            container.setItem(Slot, DisplayItem);
        }
    }


    public void onCheck() {
        if (!Init) {
            setupOrUpdateButton();
            Init = true;
        }
        if (this.container.getItem(this.Slot).isEmpty()) {
            onClick();
        }
    }

    public void reset() {
        Enabled = true;
        setupOrUpdateButton();
    }

}
