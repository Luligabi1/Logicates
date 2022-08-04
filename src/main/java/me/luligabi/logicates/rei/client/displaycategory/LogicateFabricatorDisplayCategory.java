package me.luligabi.logicates.rei.client.displaycategory;

import com.google.common.collect.Lists;
import me.luligabi.logicates.common.block.BlockRegistry;
import me.luligabi.logicates.rei.common.CommonReiPlugin;
import me.luligabi.logicates.rei.common.display.LogicateFabricatorDisplay;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.text.Text;

import java.util.List;

public class LogicateFabricatorDisplayCategory implements DisplayCategory<LogicateFabricatorDisplay> {

    @Override
    public List<Widget> setupDisplay(LogicateFabricatorDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 41, bounds.getCenterY() - 13);
        List<Widget> widgets = Lists.newArrayList();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createArrow(new Point(startPoint.x + 27, startPoint.y + 4)));
        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 61, startPoint.y + 5)));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 61, startPoint.y + 5))
                .entries(display.getOutputEntries().get(0))
                .disableBackground()
                .markOutput());

        widgets.add(Widgets.createSlot(new Point(startPoint.x - 14, startPoint.y - 13))
                .entries(display.getInputEntries().get(0)).markInput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 4, startPoint.y - 13))
                .entries(display.getInputEntries().get(1)).markInput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x - 14, startPoint.y + 5))
                .entries(display.getInputEntries().get(2)).markInput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 4, startPoint.y + 5))
                .entries(display.getInputEntries().get(3)).markInput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x - 14, startPoint.y + 23))
                .entries(display.getInputEntries().get(4)).markInput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 4, startPoint.y + 23))
                .entries(display.getInputEntries().get(5)).markInput());
        return widgets;
    }

    @Override
    public Renderer getIcon() { return EntryStacks.of(BlockRegistry.LOGICATE_FABRICATOR); }

    @Override
    public Text getTitle() {
        return Text.translatable("container.logicates.logicate_fabricator");
    }

    @Override
    public int getDisplayHeight() {
        return 72;
    }

    @Override
    public CategoryIdentifier<? extends LogicateFabricatorDisplay> getCategoryIdentifier() { return CommonReiPlugin.LOGICATE_FABRICATION; }

}