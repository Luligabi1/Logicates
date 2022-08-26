package me.luligabi.logicates.rei.common.display;

import me.luligabi.logicates.common.item.ItemRegistry;
import me.luligabi.logicates.common.misc.recipe.LogicateFabricationRecipe;
import me.luligabi.logicates.rei.common.CommonReiPlugin;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LogicateFabricatorDisplay implements Display {

    protected LogicateFabricationRecipe recipe;
    protected List<EntryIngredient> input;

    protected List<EntryIngredient> logicatePlate;
    protected List<EntryIngredient> redstone;
    protected List<EntryIngredient> redstoneTorch;
    protected List<EntryIngredient> quartz;
    protected List<EntryIngredient> lever;
    protected List<EntryIngredient> tripwireHook;


    protected List<EntryIngredient> output;

    public LogicateFabricatorDisplay(LogicateFabricationRecipe recipe) {
        this.recipe = recipe;

        this.logicatePlate = List.of(EntryIngredients.ofItems(List.of(ItemRegistry.LOGICATE_PLATE), recipe.getLogicatePlateAmount()));
        this.redstone = List.of(EntryIngredients.ofItems(List.of(Items.REDSTONE), recipe.getRedstoneAmount()));
        this.redstoneTorch = List.of(EntryIngredients.ofItems(List.of(Items.REDSTONE_TORCH), recipe.getRedstoneAmount()));
        this.quartz = List.of(EntryIngredients.ofItems(List.of(Items.QUARTZ), recipe.getQuartzAmount()));
        this.lever = List.of(EntryIngredients.ofItems(List.of(Items.LEVER), recipe.getLeverAmount()));
        this.tripwireHook = List.of(EntryIngredients.ofItems(List.of(Items.TRIPWIRE_HOOK), recipe.getTripwireAmount()));


        input = new ArrayList<>();
        input.addAll(logicatePlate);
        input.addAll(redstone);
        input.addAll(redstoneTorch);
        input.addAll(quartz);
        input.addAll(lever);
        input.addAll(tripwireHook);

        this.output = Collections.singletonList(EntryIngredients.of(recipe.getOutput()));
    }

    public LogicateFabricatorDisplay(List<EntryIngredient> input, List<EntryIngredient> output) {
        this.input = input;
        this.output = output;
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return input;
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return output;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return CommonReiPlugin.LOGICATE_FABRICATION;
    }


    public static class Serializer implements DisplaySerializer<LogicateFabricatorDisplay> {

        public static final Serializer INSTANCE = new Serializer();

        private Serializer() {}

        @Override
        public NbtCompound save(NbtCompound tag, LogicateFabricatorDisplay display) {
            NbtList input = new NbtList();
            display.input.forEach(entryStacks -> input.add(entryStacks.save()));
            tag.put("input", input);

            NbtList output = new NbtList();
            display.output.forEach(entryStacks -> output.add(entryStacks.save()));
            tag.put("output", output);

            return tag;
        }

        @Override
        public LogicateFabricatorDisplay read(NbtCompound tag) {
            List<EntryIngredient> input = new ArrayList<>();
            tag.getList("input", NbtType.LIST).forEach(nbtElement -> input.add(EntryIngredient.read((NbtList) nbtElement)));

            List<EntryIngredient> output = new ArrayList<>();
            tag.getList("output", NbtType.LIST).forEach(nbtElement -> output.add(EntryIngredient.read((NbtList) nbtElement)));

            return new LogicateFabricatorDisplay(input, output);
        }
    }

}