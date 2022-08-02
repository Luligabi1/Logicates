package me.luligabi.logicates.common.block.logicate;

import me.luligabi.logicates.common.block.SingleInputLogicateBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class NotLogicateBlock extends SingleInputLogicateBlock {

    public NotLogicateBlock() {
        super();
        setDefaultState(getStateManager().getDefaultState().with(POWERED, true));
    }

    @Override
    protected int getPower(World world, BlockPos pos, BlockState state) {
        Direction inputSide = getInputSide(state);
        return world.getEmittedRedstonePower(pos.offset(inputSide), inputSide) == 0 ? 15 : 0;
    }

}