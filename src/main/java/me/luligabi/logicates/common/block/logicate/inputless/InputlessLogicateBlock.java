package me.luligabi.logicates.common.block.logicate.inputless;

import me.luligabi.logicates.common.block.logicate.LogicateBlock;
import me.luligabi.logicates.common.block.logicate.LogicateType;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

public abstract class InputlessLogicateBlock extends LogicateBlock {

    protected InputlessLogicateBlock(FabricBlockSettings settings) {
        super(settings);
    }

    @Override
    public LogicateType getLogicateType() {
        return LogicateType.INPUTLESS;
    }

}