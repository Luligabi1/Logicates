package me.luligabi.logicates.rei.client;

import me.luligabi.logicates.common.block.BlockRegistry;
import me.luligabi.logicates.common.misc.recipe.LogicateFabricationRecipe;
import me.luligabi.logicates.rei.client.displaycategory.LogicateFabricatorDisplayCategory;
import me.luligabi.logicates.rei.common.CommonReiPlugin;
import me.luligabi.logicates.rei.common.display.LogicateFabricatorDisplay;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;

public class ClientReiPlugin implements REIClientPlugin {


    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new LogicateFabricatorDisplayCategory());
        registry.addWorkstations(CommonReiPlugin.LOGICATE_FABRICATION, EntryStacks.of(BlockRegistry.LOGICATE_FABRICATOR));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerFiller(LogicateFabricationRecipe.class, LogicateFabricatorDisplay::new);
    }

}