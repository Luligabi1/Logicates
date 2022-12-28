package me.luligabi.logicates.common.misc.recipe;

import me.luligabi.logicates.common.Logicates;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class RecipeRegistry {

    public static void init() {
        Registry.register(Registries.RECIPE_SERIALIZER, LogicateFabricationRecipeSerializer.ID, LogicateFabricationRecipeSerializer.INSTANCE);
        Registry.register(Registries.RECIPE_TYPE, Logicates.id(LogicateFabricationRecipe.Type.ID), LogicateFabricationRecipe.Type.INSTANCE);
    }

}