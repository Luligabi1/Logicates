package me.luligabi.logicates.rei.common;

import me.luligabi.logicates.common.Logicates;
import me.luligabi.logicates.common.misc.screenhandler.LogicateFabricatorScreenHandler;
import me.luligabi.logicates.rei.common.display.LogicateFabricatorDisplay;
import me.luligabi.logicates.rei.common.menuinfo.LogicateFabricatorMenuInfo;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.plugins.REIServerPlugin;
import me.shedaniel.rei.api.common.transfer.info.MenuInfoRegistry;
import me.shedaniel.rei.api.common.transfer.info.simple.SimpleMenuInfoProvider;

public class CommonReiPlugin implements REIServerPlugin {


    @Override
    public void registerMenuInfo(MenuInfoRegistry registry) {
        registry.register(LOGICATE_FABRICATION, LogicateFabricatorScreenHandler.class, SimpleMenuInfoProvider.of(LogicateFabricatorMenuInfo::new));
    }

    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        registry.register(LOGICATE_FABRICATION, LogicateFabricatorDisplay.Serializer.INSTANCE);
    }


    public static final CategoryIdentifier<LogicateFabricatorDisplay> LOGICATE_FABRICATION = CategoryIdentifier.of(Logicates.MOD_ID, "logicate_fabrication");

}