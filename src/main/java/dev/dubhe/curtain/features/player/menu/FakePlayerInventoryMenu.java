package dev.dubhe.curtain.features.player.menu;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import dev.dubhe.curtain.features.player.fakes.IServerPlayer;
import dev.dubhe.curtain.features.player.helpers.EntityPlayerActionPack;
import dev.dubhe.curtain.features.player.menu.component.Button;
import dev.dubhe.curtain.features.player.menu.component.Function;
import dev.dubhe.curtain.features.player.menu.component.RadioButtonPanel;
import dev.dubhe.curtain.features.player.menu.component.ToggledButton;
import dev.dubhe.curtain.utils.TranslationHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

public class FakePlayerInventoryMenu implements IInventory {

    public final NonNullList<ItemStack> PlayerItems;
    public final NonNullList<ItemStack> Armor;
    public final NonNullList<ItemStack> Offhand;

    private final NonNullList<ItemStack> Buttons = NonNullList.withSize(13, ItemStack.EMPTY);

    private final List<Button> ButtonList = new ArrayList<>();
    private final List<NonNullList<ItemStack>> Components;
    private final PlayerEntity player;
    private final EntityPlayerActionPack actionPack;

    public void tick() {
        this.checkButton();
    }

    public FakePlayerInventoryMenu(PlayerEntity player) {
        this.player = player;
        this.PlayerItems = player.inventory.items;
        this.Armor = player.inventory.armor;
        this.Offhand = player.inventory.offhand;
        this.actionPack = ((IServerPlayer) player).getActionPack();
        this.Components = ImmutableList.of(this.PlayerItems, this.Armor, this.Offhand, this.Buttons);
        this.createMenu();
        this.actionPack.setSlot(1);
    }

    @Override
    public int getContainerSize() {
        return this.PlayerItems.size() + this.Armor.size() + this.Offhand.size() + this.Buttons.size();
    }


    @Override
    public boolean isEmpty() {
        for (ItemStack itemStack :
                this.PlayerItems) {
            if (!itemStack.isEmpty())
                return false;
        }
        for (ItemStack itemStack :
                this.Armor) {
            if (!itemStack.isEmpty())
                return false;
        }
        for (ItemStack itemStack :
                this.Offhand) {
            if (!itemStack.isEmpty())
                return false;
        }

        return true;
    }

    @Override
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
                return new Pair<>(PlayerItems, slot - 9);
            }
            case 45, 46, 47, 48, 49, 50, 51, 52, 53 -> {
                return new Pair<>(PlayerItems, slot - 45);
            }
            default -> {
                return null;
            }
        }
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        Pair<NonNullList<ItemStack>, Integer> pair = getItemSlot(slot);
        NonNullList<ItemStack> list = null;
        if (pair != null) {
            list = pair.getFirst();
            slot = pair.getSecond();
        }
        if (list != null && !list.get(slot).isEmpty()) {
            return ItemStackHelper.removeItem(list, slot, amount);
        }
        return ItemStack.EMPTY;
    }

    @Override
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
    public void setItem(int slot, ItemStack stack) {
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
    public boolean stillValid(PlayerEntity player) {
        if (!this.player.isAlive())
            return false;
        return !(player.distanceToSqr(this.player) > 64.0d);
    }

    @Override
    public void clearContent() {
        for (var list :
                this.Components) {
            list.clear();
        }
    }

    private void createMenu() {
        List<Pair<Integer, Integer>> slots_and_itemCounts = new ArrayList<>();
        List<Function> functions = new ArrayList<>();
        List<Pair<IFormattableTextComponent, IFormattableTextComponent>> components = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            slots_and_itemCounts.add(new Pair<>(i + 9, i + 1));
            int finalI = i + 1;
            functions.add(() -> actionPack.setSlot(finalI));
            components.add(new Pair<>(
                    TranslationHelper.translate("curtain.rules.open_fake_player_inventory.menu.slot", i + 1).withStyle(s -> s.withColor(TextFormatting.RED).withBold(true).withItalic(false)),
                    TranslationHelper.translate("curtain.rules.open_fake_player_inventory.menu.slot", i + 1).withStyle(s -> s.withColor(TextFormatting.WHITE).withBold(true).withItalic(false))
            ));
        }

        RadioButtonPanel panel = new RadioButtonPanel(
                Items.GLASS,
                Items.WHITE_CONCRETE,
                slots_and_itemCounts,
                functions,
                components,
                this
        );
        ButtonList.add(panel);


        Button stop_all_button = new Button(
                TranslationHelper.translate("curtain.rules.open_fake_player_inventory.menu.stop_all")
                        .withStyle(style -> style
                                .withColor(TextFormatting.WHITE)
                                .withBold(true)
                                .withItalic(false)),
                this,
                0
        ); // 停止所有动作


        ToggledButton attackInterval14 = new ToggledButton(
                Items.BARRIER,
                Items.STRUCTURE_VOID,
                TranslationHelper.translate("curtain.rules.open_fake_player_inventory.menu.attack_interval_14", "ON")
                        .withStyle(s -> s
                                .withColor(TextFormatting.GRAY)
                                .withBold(true)
                                .withItalic(false)),
                TranslationHelper.translate("curtain.rules.open_fake_player_inventory.menu.attack_interval_14", "OFF")
                        .withStyle(s -> s
                                .withColor(TextFormatting.WHITE)
                                .withBold(true)
                                .withItalic(false)),
                1,
                this,
                5
        );

        ToggledButton attackContinuous = new ToggledButton(
                Items.BARRIER,
                Items.STRUCTURE_VOID,
                TranslationHelper.translate("curtain.rules.open_fake_player_inventory.menu.attack_continuous", "ON")
                        .withStyle(s -> s
                                .withColor(TextFormatting.GRAY)
                                .withBold(true)
                                .withItalic(false)),
                TranslationHelper.translate("curtain.rules.open_fake_player_inventory.menu.attack_continuous", "OFF")
                        .withStyle(s -> s
                                .withColor(TextFormatting.WHITE)
                                .withBold(true)
                                .withItalic(false)),
                1,
                this,
                6
        );

        ToggledButton useContinuous = new ToggledButton(
                Items.BARRIER,
                Items.STRUCTURE_VOID,
                TranslationHelper.translate("curtain.rules.open_fake_player_inventory.menu.use_continuous", "ON")
                        .withStyle(s -> s
                                .withColor(TextFormatting.GRAY)
                                .withBold(true)
                                .withItalic(false)),
                TranslationHelper.translate("curtain.rules.open_fake_player_inventory.menu.use_continuous", "OFF")
                        .withStyle(s -> s
                                .withColor(TextFormatting.WHITE)
                                .withBold(true)
                                .withItalic(false)),
                1,
                this,
                8
        );

        stop_all_button.addClickEvent(() -> {
            attackInterval14.reset();
            attackContinuous.reset();
            useContinuous.reset();
            actionPack.stopAll();
        });

        attackInterval14.addClickEvent(() -> actionPack.start(EntityPlayerActionPack.ActionType.ATTACK, EntityPlayerActionPack.Action.interval(14)));
        attackInterval14.addToggledOffEvent(() -> actionPack.start(EntityPlayerActionPack.ActionType.ATTACK, EntityPlayerActionPack.Action.once()));

        attackContinuous.addClickEvent(() -> actionPack.start(EntityPlayerActionPack.ActionType.ATTACK, EntityPlayerActionPack.Action.continuous()));
        attackContinuous.addToggledOffEvent(() -> actionPack.start(EntityPlayerActionPack.ActionType.ATTACK, EntityPlayerActionPack.Action.once()));

        useContinuous.addClickEvent(() -> actionPack.start(EntityPlayerActionPack.ActionType.USE, EntityPlayerActionPack.Action.continuous()));
        useContinuous.addToggledOffEvent(() -> actionPack.start(EntityPlayerActionPack.ActionType.USE, EntityPlayerActionPack.Action.once()));


        ButtonList.add(stop_all_button);
        ButtonList.add(attackInterval14);
        ButtonList.add(attackContinuous);
        ButtonList.add(useContinuous);
    }

    private void checkButton() {
        for (var button :
                ButtonList) {
            button.onCheck();
        }
    }
}
