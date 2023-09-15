package me.luligabi.logicates.common.block.logicate.misc;

import me.luligabi.logicates.common.block.logicate.LogicateBlock;
import me.luligabi.logicates.common.block.logicate.LogicateType;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

public abstract class MiscellaneousLogicateBlock extends LogicateBlock {

    public MiscellaneousLogicateBlock(FabricBlockSettings settings) {
        super(settings);
    }

    @Override
    public LogicateType getLogicateType() {
        return LogicateType.MISCELLANEOUS;
    }

}