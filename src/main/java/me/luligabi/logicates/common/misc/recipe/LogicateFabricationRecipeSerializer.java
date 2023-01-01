package me.luligabi.logicates.common.misc.recipe;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import me.luligabi.logicates.common.Logicates;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class LogicateFabricationRecipeSerializer implements RecipeSerializer<LogicateFabricationRecipe> {

    private LogicateFabricationRecipeSerializer() {
    }

    public static final LogicateFabricationRecipeSerializer INSTANCE = new LogicateFabricationRecipeSerializer();

    public static final Identifier ID = Logicates.id("logicate_fabrication");

    @Override // Turns json into Recipe
    public LogicateFabricationRecipe read(Identifier recipeId, JsonObject json) {
        LogicateFabricationRecipeJsonFormat recipeJson = new Gson().fromJson(json, LogicateFabricationRecipeJsonFormat.class);
        if(recipeJson.outputItem == null) {
            throw new JsonSyntaxException("A required attribute is missing!");
        }
        if(recipeJson.outputAmount == 0) recipeJson.outputAmount = 1;


        Item outputItem = Registries.ITEM.getOrEmpty(new Identifier(recipeJson.outputItem))
                .orElseThrow(() -> new JsonSyntaxException("No such item " + recipeJson.outputItem));
        ItemStack output = new ItemStack(outputItem, recipeJson.outputAmount);

        return new LogicateFabricationRecipe(
                output,
                recipeJson.outputAmount,
                recipeJson.logicatePlateAmount,
                recipeJson.redstoneAmount,
                recipeJson.redstoneTorchAmount,
                recipeJson.quartzAmount,
                recipeJson.leverAmount,
                recipeJson.tripwireAmount,
                recipeId
        );
    }

    @Override // Turns Recipe into PacketByteBuf
    public void write(PacketByteBuf packetData, LogicateFabricationRecipe recipe) {
        packetData.writeItemStack(new ItemStack(recipe.getOutput().getItem(), recipe.getOutputAmount()));
        packetData.writeInt(recipe.getOutputAmount());
        packetData.writeInt(recipe.getLogicatePlateAmount());
        packetData.writeInt(recipe.getRedstoneAmount());
        packetData.writeInt(recipe.getRedstoneTorchAmount());
        packetData.writeInt(recipe.getQuartzAmount());
        packetData.writeInt(recipe.getLeverAmount());
        packetData.writeInt(recipe.getTripwireAmount());
    }

    @Override // Turns PacketByteBuf into Recipe
    public LogicateFabricationRecipe read(Identifier recipeId, PacketByteBuf packetData) {
        ItemStack output = packetData.readItemStack();
        int outputAmount = packetData.readInt();
        int logicatePlateAmount = packetData.readInt();
        int redstoneAmount = packetData.readInt();
        int redstoneTorchAmount = packetData.readInt();
        int quartzAmount = packetData.readInt();
        int leverAmount = packetData.readInt();
        int tripwireAmount = packetData.readInt();
        return new LogicateFabricationRecipe(
                output,
                outputAmount,
                logicatePlateAmount,
                redstoneAmount,
                redstoneTorchAmount,
                quartzAmount,
                leverAmount,
                tripwireAmount,
                recipeId
        );
    }

}