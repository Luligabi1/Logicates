package me.luligabi.logicates.common.block.logicate.input.single;

import me.luligabi.logicates.common.block.logicate.input.InputtableLogicateBlock;
import me.luligabi.logicates.common.block.property.InputSides;
import me.luligabi.logicates.common.block.property.PropertyRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public abstract class SingleInputLogicateBlock extends InputtableLogicateBlock {

    protected SingleInputLogicateBlock() {
        super();
        setDefaultState(getStateManager().getDefaultState().with(FACING, Direction.NORTH).with(POWERED, false).with(getInputSideProperty(), InputSides.BACK));
    }

    @Override
    protected EnumProperty<InputSides> getInputSideProperty() {
        return PropertyRegistry.SINGLE_INPUT_SIDE;
    }

    public Direction getInputSide(BlockState state) {
        Direction direction = state.get(FACING);
        switch(state.get(getInputSideProperty())) {
            case LEFT -> {
                return getInputSideDirection(state, true);
            }
            case RIGHT -> {
                return getInputSideDirection(state, false);
            }
            case BACK -> {
                return direction;
            }
            case LEFT_RIGHT, LEFT_BACK, RIGHT_BACK -> throw new IllegalStateException("Illegal dual input blockstate on single input Logicate!");
        }
        return direction;
    }

    protected int getInput(World world, BlockPos pos, BlockState state) {
        Direction inputSide = getInputSide(state);
        return world.getEmittedRedstonePower(pos.offset(inputSide), inputSide);
    }

}