package me.luligabi.logicates.common.misc.recipe;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class LogicateFabricationRecipe implements Recipe<Inventory> {

    private final ItemStack outputStack;
    private final int outputAmount;

    private final int logicatePlateAmount;
    private final int redstoneAmount;
    private final int redstoneTorchAmount;
    private final int quartzAmount;
    private final int leverAmount;
    private final int tripwireAmount;

    private final Identifier identifier;


    public LogicateFabricationRecipe(ItemStack outputStack, int outputAmount, int logicatePlateAmount, int redstoneAmount, int redstoneTorchAmount, int quartzAmount, int leverAmount, int tripwireAmount, Identifier identifier) {
        this.outputStack = outputStack;
        this.outputAmount = outputAmount;
        this.logicatePlateAmount = logicatePlateAmount;
        this.redstoneAmount = redstoneAmount;
        this.redstoneTorchAmount = redstoneTorchAmount;
        this.quartzAmount = quartzAmount;
        this.leverAmount = leverAmount;
        this.tripwireAmount = tripwireAmount;
        this.identifier = identifier;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return getOutput();
    }

    public ItemStack getOutput() {
        return outputStack.copy();
    }

    public int getOutputAmount() {
        return outputAmount;
    }

    public int getLogicatePlateAmount() {
        return logicatePlateAmount;
    }

    public int getRedstoneAmount() {
        return redstoneAmount;
    }

    public int getRedstoneTorchAmount() {
        return redstoneTorchAmount;
    }

    public int getQuartzAmount() {
        return quartzAmount;
    }

    public int getLeverAmount() {
        return leverAmount;
    }

    public int getTripwireAmount() {
        return tripwireAmount;
    }

    @Override
    public boolean matches(Inventory inventory, World world) {
        return hasRequiredMaterial(inventory, 0, logicatePlateAmount) &&
                hasRequiredMaterial(inventory, 1, redstoneAmount) &&
                hasRequiredMaterial(inventory, 2, redstoneTorchAmount) &&
                hasRequiredMaterial(inventory, 3, quartzAmount) &&
                hasRequiredMaterial(inventory, 4, leverAmount) &&
                hasRequiredMaterial(inventory, 5, tripwireAmount);
    }

    public int getInputAmountByIndex(int index) {
        return switch(index) {
            case 0 -> logicatePlateAmount;
            case 1 -> redstoneAmount;
            case 2 -> redstoneTorchAmount;
            case 3 -> quartzAmount;
            case 4 -> leverAmount;
            case 5 -> tripwireAmount;
            default -> throw new IndexOutOfBoundsException(String.format("Input index %d does not exist!", index));
        };
    }

    @Override
    public ItemStack craft(Inventory inventory, DynamicRegistryManager registryManager) {
        return getOutput();
    }

    @Override
    public boolean fits(int var1, int var2) {
        return false;
    }

    @Override
    public Identifier getId() {
        return identifier;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return LogicateFabricationRecipeSerializer.INSTANCE;
    }

    public static class Type implements RecipeType<LogicateFabricationRecipe> {
        private Type() {}
        public static final Type INSTANCE = new Type();

        public static final String ID = "logicate_fabrication";
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    @Override
    public boolean isIgnoredInRecipeBook() {
        return true;
    }

    private boolean hasRequiredMaterial(Inventory inventory, int index, int requiredMaterial) {
        return inventory.getStack(index).getCount() >= requiredMaterial;
    }

}