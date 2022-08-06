package me.luligabi.logicates.common.block.logicate.inputless;

import me.luligabi.logicates.common.block.logicate.LogicateBlock;
import me.luligabi.logicates.common.block.logicate.LogicateType;

public abstract class InputlessLogicateBlock extends LogicateBlock {

    @Override
    protected LogicateType getLogicateType() {
        return LogicateType.INPUTLESS;
    }

}