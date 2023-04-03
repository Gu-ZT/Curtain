package dev.dubhe.curtain.features.player.menu;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import dev.dubhe.curtain.features.player.fakes.IServerPlayer;
import dev.dubhe.curtain.features.player.helpers.EntityPlayerActionPack;
import dev.dubhe.curtain.features.player.menu.component.Button;
import dev.dubhe.curtain.features.player.menu.component.Function;
import dev.dubhe.curtain.features.player.menu.component.RadioButtonPanel;
import dev.dubhe.curtain.features.player.menu.component.ToggledButton;
import dev.dubhe.curtain.utils.Messenger;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FakePlayerInventoryMenu implements Container {

    public final NonNullList<ItemStack> Items;
    public final NonNullList<ItemStack> Armor;
    public final NonNullList<ItemStack> Offhand;

    private final NonNullList<ItemStack> Buttons = NonNullList.withSize(13,ItemStack.EMPTY);

    private final List<Button> ButtonList = new ArrayList<>();
    private final List<NonNullList<ItemStack>> Components;
    private final Player player;
    private final EntityPlayerActionPack actionPack;

    public void tick(){
        this.checkButton();
    }

    public FakePlayerInventoryMenu(Player player){
        this.player = player;
        this.Items = player.getInventory().items;
        this.Armor = player.getInventory().armor;
        this.Offhand = player.getInventory().offhand;
        this.actionPack = ((IServerPlayer)player).getActionPack();
        this.Components = ImmutableList.of(this.Items,this.Armor,this.Offhand,this.Buttons);
        this.createMenu();
        this.actionPack.setSlot(1);
    }

    @Override
    public int getContainerSize() {
        return this.Items.size()+this.Armor.size()+this.Offhand.size()+this.Buttons.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemStack:
             this.Items) {
            if(!itemStack.isEmpty())
                return false;
        }
        for (ItemStack itemStack:
                this.Armor) {
            if(!itemStack.isEmpty())
                return false;
        }
        for (ItemStack itemStack:
                this.Offhand) {
            if(!itemStack.isEmpty())
                return false;
        }

        return true;
    }

    @Override
    @NotNull
    public ItemStack getItem(int slot) {
        Pair<NonNullList<ItemStack>, Integer> pair = getItemSlot(slot);
        if (pair != null) {
            return pair.getFirst().get(pair.getSecond());
        } else {
            return ItemStack.EMPTY;
        }
    }

    public Pair<NonNullList<ItemStack>, Integer> getItemSlot(int slot) {
        switch (slot) {
            case 0 -> {
                return new Pair<>(Buttons, 0);
            }
            case 1, 2, 3, 4 -> {
                return new Pair<>(Armor, 4 - slot);
            }
            case 5, 6 -> {
                return new Pair<>(Buttons, slot - 4);
            }
            case 7 -> {
                return new Pair<>(Offhand, 0);
            }
            case 8, 9, 10, 11, 12, 13, 14, 15, 16, 17 -> {
                return new Pair<>(Buttons, slot - 5);
            }
            case 18, 19, 20, 21, 22, 23, 24, 25, 26,
                    27, 28, 29, 30, 31, 32, 33, 34, 35,
                    36, 37, 38, 39, 40, 41, 42, 43, 44 -> {
                return new Pair<>(Items, slot - 9);
            }
            case 45, 46, 47, 48, 49, 50, 51, 52, 53 -> {
                return new Pair<>(Items, slot - 45);
            }
            default -> {
                return null;
            }
        }
    }

    @Override
    @NotNull
    public ItemStack removeItem(int slot, int amount) {
        Pair<NonNullList<ItemStack>, Integer> pair = getItemSlot(slot);
        NonNullList<ItemStack> list = null;
        if (pair != null) {
            list = pair.getFirst();
            slot = pair.getSecond();
        }
        if (list != null && !list.get(slot).isEmpty()) {
            return ContainerHelper.removeItem(list, slot, amount);
        }
        return ItemStack.EMPTY;
    }

    @Override
    @NotNull
    public ItemStack removeItemNoUpdate(int slot) {
        Pair<NonNullList<ItemStack>, Integer> pair = getItemSlot(slot);
        NonNullList<ItemStack> list = null;
        if (pair != null) {
            list = pair.getFirst();
            slot = pair.getSecond();
        }
        if (list != null && !list.get(slot).isEmpty()) {
            ItemStack itemStack = list.get(slot);
            list.set(slot, ItemStack.EMPTY);
            return itemStack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int slot, @NotNull ItemStack stack) {
        Pair<NonNullList<ItemStack>, Integer> pair = getItemSlot(slot);
        NonNullList<ItemStack> list = null;
        if (pair != null) {
            list = pair.getFirst();
            slot = pair.getSecond();
        }
        if (list != null) {
            list.set(slot, stack);
        }
    }

    @Override
    public void setChanged() {
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        if(this.player.isRemoved())
            return false;
        return !(player.distanceToSqr(this.player)>64.0d);
    }

    @Override
    public void clearContent() {
        for (var list :
                this.Components) {
            list.clear();
        }
    }

    private void createMenu(){
        List<Pair<Integer,Integer>> slots_and_itemCounts = new ArrayList<>();
        List<Function> functions = new ArrayList<>();
        List<Pair<Component,Component>> components = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            slots_and_itemCounts.add(new Pair<>(i+9,i+1));
            int finalI = i+1;
            functions.add(()->actionPack.setSlot(finalI));
            components.add(new Pair<>(Messenger.c("wb Slot ",i+1),Messenger.c("wb Slot ",i+1)));
        }

        RadioButtonPanel panel = new RadioButtonPanel(
                net.minecraft.world.item.Items.GREEN_STAINED_GLASS,
                net.minecraft.world.item.Items.RED_STAINED_GLASS,
                slots_and_itemCounts,
                functions,
                components,
                this
        );
        ButtonList.add(panel);


        Button stop_all_button = new Button(Messenger.c("wb Stop All Action"),this,0); // 停止所有动作
        stop_all_button.addClickEvent(actionPack::stopAll);
        ButtonList.add(stop_all_button);






    }

    private void checkButton(){
        for (var button :
                ButtonList) {
            button.onCheck();
        }
    }
}
