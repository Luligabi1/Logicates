package me.luligabi.logicates.common.misc.screenhandler;

import com.google.common.collect.Lists;
import me.luligabi.logicates.common.item.ItemRegistry;
import me.luligabi.logicates.common.misc.recipe.LogicateFabricationRecipe;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

import java.util.List;

public class LogicateFabricatorScreenHandler extends ScreenHandler {


    public LogicateFabricatorScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public LogicateFabricatorScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ScreenHandlingRegistry.LOGICATE_FABRICATOR_SCREEN_HANDLER, syncId);
        this.selectedRecipe = Property.create();
        this.availableRecipes = Lists.newArrayList();
        this.contentsChangedListener = () -> {
        };
        this.input = new SimpleInventory(6) {
            public void markDirty() {
                super.markDirty();
                LogicateFabricatorScreenHandler.this.onContentChanged(this);
                LogicateFabricatorScreenHandler.this.contentsChangedListener.run();
            }
        };
        this.output = new CraftingResultInventory();
        this.context = context;
        this.world = playerInventory.player.world;
        this.addSlot(new InputSlot(input, 0, 10, 16, new ItemStack(ItemRegistry.LOGICATE_PLATE)));
        this.addSlot(new InputSlot(input, 1, 28, 16, ConventionalItemTags.REDSTONE_DUSTS));
        this.addSlot(new InputSlot(input, 2, 10, 34, new ItemStack(Blocks.REDSTONE_TORCH)));
        this.addSlot(new InputSlot(input, 3, 28, 34, ConventionalItemTags.QUARTZ));
        this.addSlot(new InputSlot(input, 4, 10, 52, new ItemStack(Blocks.LEVER)));
        this.addSlot(new InputSlot(input, 5, 28, 52, new ItemStack(Blocks.TRIPWIRE_HOOK)));
        this.outputSlot = this.addSlot(new Slot(this.output, 6, 143, 33) {
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                stack.onCraft(player.world, player, stack.getCount());
                LogicateFabricatorScreenHandler.this.output.unlockLastRecipe(player);

                LogicateFabricationRecipe logicateFabricationRecipe = availableRecipes.get(getSelectedRecipe());
                for(int i = 0; i < 6; i++) {
                    ItemStack itemStack = LogicateFabricatorScreenHandler.this.input.getStack(i);
                    itemStack.decrement(logicateFabricationRecipe.getInputAmountByIndex(i));
                }

                if(!logicateFabricationRecipe.matches(input, world)) {
                    LogicateFabricatorScreenHandler.this.refreshAvailableRecipes(input);
                } else {
                    outputSlot.setStack(logicateFabricationRecipe.getOutput());
                }


                context.run((world, pos) -> {
                    long l = world.getTime();
                    if (LogicateFabricatorScreenHandler.this.lastTakeTime != l) {
                        world.playSound(null, pos, SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        LogicateFabricatorScreenHandler.this.lastTakeTime = l;
                    }

                });
                super.onTakeItem(player, stack);
            }
        });

        int i;
        for(i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }

        this.addProperty(selectedRecipe);
    }

    public int getSelectedRecipe() {
        return selectedRecipe.get();
    }

    public List<LogicateFabricationRecipe> getAvailableRecipes() {
        return availableRecipes;
    }

    public int getAvailableRecipeCount() {
        return availableRecipes.size();
    }


    public boolean canCraft() {
        return !input.isEmpty() && !availableRecipes.isEmpty();
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        if (isInBounds(id)) {
            selectedRecipe.set(id);
            populateResult();
        }

        return true;
    }

    private boolean isInBounds(int id) {
        return id >= 0 && id < availableRecipes.size();
    }

    public void onContentChanged(Inventory inventory) {
        if(!inventory.isEmpty()) updateInput(inventory);
    }

    private void updateInput(Inventory input) {
        this.selectedRecipe.set(-1);
        this.outputSlot.setStack(ItemStack.EMPTY);
        refreshAvailableRecipes(input);
    }

    private void refreshAvailableRecipes(Inventory input) {
        this.availableRecipes.clear();
        this.availableRecipes = world.getRecipeManager().getAllMatches(LogicateFabricationRecipe.Type.INSTANCE, input, world);
    }

    private void populateResult() {
        if (!availableRecipes.isEmpty() && isInBounds(selectedRecipe.get())) {
            LogicateFabricationRecipe logicateFabricationRecipe = availableRecipes.get(getSelectedRecipe());
            output.setLastRecipe(logicateFabricationRecipe);
            outputSlot.setStack(logicateFabricationRecipe.craft(input, world.getRegistryManager()));
        } else {
            outputSlot.setStack(ItemStack.EMPTY);
        }

        sendContentUpdates();
    }

    public void setContentsChangedListener(Runnable contentsChangedListener) {
        this.contentsChangedListener = contentsChangedListener;
    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.inventory != output && super.canInsertIntoSlot(stack, slot);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if(slot != null && slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if(index == 6) {
                this.context.run((world, pos) -> itemStack2.getItem().onCraft(itemStack2, world, player));
                if (!this.insertItem(itemStack2, 10, 43, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickTransfer(itemStack2, itemStack);
            } else if(index >= 7 && index < 42) {
                if(!this.insertItem(itemStack2, 0, 6, false)) {
                    if(index < 37) {
                        if(!this.insertItem(itemStack2, 37, 41, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if(!this.insertItem(itemStack2, 7, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if(!this.insertItem(itemStack2, 7, 41, false)) {
                return ItemStack.EMPTY;
            }

            if(itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(player, itemStack2);
            if (index == 6) {
                player.dropItem(itemStack2, false);
            }
        }

        return itemStack;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        output.removeStack(6);
        context.run((world, pos) -> dropInventory(player, input));
    }


    private final ScreenHandlerContext context;
    private final Property selectedRecipe;
    private final World world;
    private List<LogicateFabricationRecipe> availableRecipes;
    long lastTakeTime;
    final Slot outputSlot;
    Runnable contentsChangedListener;
    public final Inventory input;
    private final CraftingResultInventory output;


    private static class InputSlot extends Slot {

        public InputSlot(Inventory inventory, int index, int x, int y, ItemStack stack) {
            super(inventory, index, x, y);
            this.itemStack = stack;
            this.usesTag = false;

            this.tag = null;
        }

        public InputSlot(Inventory inventory, int index, int x, int y, TagKey<Item> tag) {
            super(inventory, index, x, y);
            this.tag = tag;
            this.usesTag = true;

            this.itemStack = ItemStack.EMPTY;
        }

        public boolean canInsert(ItemStack stack) {
            return usesTag ? stack.isIn(tag) : stack.isOf(itemStack.getItem());
        }

        private final ItemStack itemStack;
        private final TagKey<Item> tag;
        private final boolean usesTag;

    }

}