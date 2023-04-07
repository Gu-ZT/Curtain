package dev.dubhe.curtain.features.player.menu.component;

import com.mojang.datafixers.util.Pair;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class RadioButtonPanel extends Button{
    private List<Pair<Integer, Integer>> SlotsAndItemCounts;
    private List<Function> FunctionsForEveryButton; // Turn on Event,Turn off Event

    private List<Pair<Component,Component>> TextForEveryButton; // ON / OFF

    private Item ToggledOff_DisplayItem;

    private int selected_Button = 0;

    public RadioButtonPanel(Item ToggledOn_DisplayItem,Item toggledOff_DisplayItem ,List<Pair<Integer, Integer>> slotsAndItemCounts, List<Function> functions,List<Pair<Component,Component>> text,Container container) {
        super(true, ToggledOn_DisplayItem, 0, new TextComponent(""), container, 0);
        this.SlotsAndItemCounts = slotsAndItemCounts;
        this.FunctionsForEveryButton = functions;
        this.ToggledOff_DisplayItem = toggledOff_DisplayItem;
        this.TextForEveryButton = text;
    }

    @Override
    protected void setupOrUpdateButton() {
        if(SlotsAndItemCounts == null)
            return;
        for (int i = 0; i < this.SlotsAndItemCounts.size(); i++) {
            setupButton(i);
        }
    }

    protected void setupButton(int i){
        ItemStack DisplayItem = new ItemStack(
                Enabled?
                        selected_Button == i?
                                this.Enabled_DisplayItem:
                                this.ToggledOff_DisplayItem
                        :this.Disabled_DisplayItem,
                SlotsAndItemCounts.get(i).getSecond()
        );

        DisplayItem.setTag(compoundTag.copy());
        DisplayItem.setHoverName(
                selected_Button == i?
                        this.TextForEveryButton.get(i).getFirst():
                        this.TextForEveryButton.get(i).getSecond()
        );

        int slot = SlotsAndItemCounts.get(i).getFirst();
        ItemStack slotItem = Container.getItem(slot);
        if(slotItem.is(this.Enabled_DisplayItem) || slotItem.is(this.Disabled_DisplayItem) || slotItem.is(this.ToggledOff_DisplayItem) || slotItem.isEmpty()){
            Container.setItem(slot,DisplayItem);
        }
    }

    @Override
    public void onCheck() {
        if(!Init){
            setupOrUpdateButton();
            Init = true;
        }
        for (int i = 0; i < this.SlotsAndItemCounts.size(); i++) {
            int slot = this.SlotsAndItemCounts.get(i).getFirst();
            if(Container.getItem(slot).isEmpty()){
                if(i == selected_Button){
                    setupButton(i);
                }else{
                    this.FunctionsForEveryButton.get(i).accept();
                    int prev_button = selected_Button;
                    selected_Button = i;
                    setupButton(i);
                    setupButton(prev_button);
                }
            }
        }
    }
}
