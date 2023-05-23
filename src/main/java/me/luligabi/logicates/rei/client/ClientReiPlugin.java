package me.luligabi.logicates.rei.client;

import me.luligabi.logicates.client.screen.KeypadLogicateScreen;
import me.luligabi.logicates.common.block.BlockRegistry;
import me.luligabi.logicates.common.item.ItemRegistry;
import me.luligabi.logicates.common.misc.recipe.LogicateFabricationRecipe;
import me.luligabi.logicates.rei.client.displaycategory.LogicateFabricatorDisplayCategory;
import me.luligabi.logicates.rei.common.CommonReiPlugin;
import me.luligabi.logicates.rei.common.display.LogicateFabricatorDisplay;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.entry.EntryRegistry;
import me.shedaniel.rei.api.client.registry.screen.OverlayDecider;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ActionResult;

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

    @Override
    public void registerEntries(EntryRegistry registry) {
        registry.removeEntry(EntryStacks.of(ItemRegistry.TIMER_LOGICATE_POINTER));
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerDecider(new OverlayDecider() {

            @Override
            public <R extends Screen> boolean isHandingScreen(Class<R> screen) {
                return screen == KeypadLogicateScreen.class;
            }

            @Override
            public <R extends Screen> ActionResult shouldScreenBeOverlaid(R screen) {
                return ActionResult.FAIL;
            }
        });
    }
}