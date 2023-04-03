package dev.dubhe.curtain.features.player.menu.component;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public class Button {
    protected boolean Enabled; // 是否启用
    protected final Item Enabled_DisplayItem; // 按钮启用展示的物品
    protected final Item Disabled_DisplayItem; // 按钮禁用时的物品
    protected final int ItemCount; // 展示物品数量
    protected final Component Text; // 展示的文本
    protected final List<Function> Functions = new ArrayList<>(); // 点击事件

    protected final Container Container; // 注冊的界面
    protected final int Slot; // 注冊界面的槽位

    protected boolean Init = false; // 是否完成加载
    CompoundTag compoundTag = new CompoundTag();


    public Button(Component text,Container container,int slot){
        this(true,Items.COMMAND_BLOCK,1,text,container,slot);
    }
    public Button(boolean isEnable, Item enabled_DisplayItem, int itemCount, Component text, Container container,int slot){
        this(isEnable,enabled_DisplayItem,itemCount,text,Items.BARRIER,container,slot);
    }

    public Button(boolean isEnable,Item displayItem,int itemCount,Component text,Item disabled_DisplayItem,Container container,int slot){
        this.Enabled = isEnable;
        this.Enabled_DisplayItem = displayItem;
        this.Disabled_DisplayItem = disabled_DisplayItem;
        this.ItemCount = itemCount;
        this.Text = text;
        this.Container = container;
        this.Slot = slot;
        this.compoundTag.putBoolean("CurtainGUIItem",true);
    }

    protected void onClick(){
        if(Enabled){
            for (var func :
                    this.Functions) {
                func.accept();
            }
        }
        setupOrUpdateButton();
    }

    public void addClickEvent(Function func){
        this.Functions.add(func);
    }

    public void setEnabled(boolean enabled){
        this.Enabled = enabled;
        setupOrUpdateButton();
    }

    protected void setupOrUpdateButton(){
        ItemStack DisplayItem = new ItemStack(
                Enabled?this.Enabled_DisplayItem:this.Disabled_DisplayItem,
                this.ItemCount
        );
        DisplayItem.setTag(compoundTag.copy());
        DisplayItem.setHoverName(this.Text);

        ItemStack slotItem = Container.getItem(Slot);
        if(slotItem.is(this.Enabled_DisplayItem) || slotItem.is(this.Disabled_DisplayItem) || slotItem.isEmpty()){
            Container.setItem(Slot,DisplayItem);
        }
    }

    public int getSlot(){
        return this.Slot;
    }

    public void onCheck(){
        if(!Init){
            setupOrUpdateButton();
            Init = true;
        }
        if (this.Container.getItem(this.Slot).isEmpty()) {
            onClick();
        }
    }

}
