package me.luligabi.logicates.common.misc.recipe;

import me.luligabi.logicates.common.Logicates;
import net.minecraft.util.registry.Registry;

public class RecipeRegistry {

    public static void init() {
        Registry.register(Registry.RECIPE_SERIALIZER, LogicateFabricationRecipeSerializer.ID, LogicateFabricationRecipeSerializer.INSTANCE);
        Registry.register(Registry.RECIPE_TYPE, Logicates.id(LogicateFabricationRecipe.Type.ID), LogicateFabricationRecipe.Type.INSTANCE);
    }

}