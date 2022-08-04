package me.luligabi.logicates.rei.common.menuinfo;

import me.luligabi.logicates.common.misc.screenhandler.LogicateFabricatorScreenHandler;
import me.luligabi.logicates.rei.common.display.LogicateFabricatorDisplay;
import me.shedaniel.rei.api.common.transfer.info.MenuInfoContext;
import me.shedaniel.rei.api.common.transfer.info.clean.InputCleanHandler;
import me.shedaniel.rei.api.common.transfer.info.simple.SimplePlayerInventoryMenuInfo;
import me.shedaniel.rei.api.common.transfer.info.stack.SlotAccessor;

import java.util.ArrayList;
import java.util.List;

public class LogicateFabricatorMenuInfo implements SimplePlayerInventoryMenuInfo<LogicateFabricatorScreenHandler, LogicateFabricatorDisplay> {


    public LogicateFabricatorMenuInfo(LogicateFabricatorDisplay display) {
        this.display = display;
    }

    protected final LogicateFabricatorDisplay display;


    @Override
    public Iterable<SlotAccessor> getInputSlots(MenuInfoContext<LogicateFabricatorScreenHandler, ?, LogicateFabricatorDisplay> context) {
        List<SlotAccessor> list = new ArrayList<>(4);
        for (int i = 0; i <= 3; i++) {
            list.add(SlotAccessor.fromContainer(context.getMenu().input, i));
        }

        return list;
    }

    public InputCleanHandler<LogicateFabricatorScreenHandler, LogicateFabricatorDisplay> getInputCleanHandler() {
        return context -> {
            LogicateFabricatorScreenHandler handler = context.getMenu();
            for (SlotAccessor gridStack : getInputSlots(context)) {
                InputCleanHandler.returnSlotsToPlayerInventory(context, getDumpHandler(), gridStack);
            }

            clearInputSlots(handler);
        };
    }

    @Override
    public LogicateFabricatorDisplay getDisplay() {
        return display;
    }

}
