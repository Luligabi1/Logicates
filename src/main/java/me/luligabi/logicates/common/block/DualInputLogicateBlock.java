package me.luligabi.logicates.common.block;

import me.luligabi.logicates.common.block.property.InputSides;
import me.luligabi.logicates.common.block.property.PropertyRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public abstract class DualInputLogicateBlock extends LogicateBlock {

    protected DualInputLogicateBlock() {
        super();
        setDefaultState(getStateManager().getDefaultState().with(FACING, Direction.NORTH).with(POWERED, false).with(getInputSideProperty(), InputSides.LEFT_RIGHT));
    }

    @Override
    EnumProperty<InputSides> getInputSideProperty() {
        return PropertyRegistry.DUAL_INPUT_SIDE;
    }

    public Pair<Direction, Direction> getInputSides(BlockState state) {
        Direction direction = state.get(FACING);
        switch(state.get(getInputSideProperty())) {
            case LEFT_RIGHT -> {
                return new Pair<>(getInputSideDirection(state, true), getInputSideDirection(state, false));
            }
            case LEFT_BACK -> {
                return new Pair<>(getInputSideDirection(state, true), direction);
            }
            case RIGHT_BACK -> {
                return new Pair<>(getInputSideDirection(state, false), direction);
            }
            case LEFT, RIGHT, BACK -> throw new IllegalStateException("Illegal single input blockstate on dual input Logicate!");
        }
        return new Pair<>(direction, direction);
    }

    protected int getInputA(World world, BlockPos pos, BlockState state) {
        Pair<Direction, Direction> inputSides = getInputSides(state);
        return world.getEmittedRedstonePower(pos.offset(inputSides.getLeft()), inputSides.getLeft());
    }

    protected int getInputB(World world, BlockPos pos, BlockState state) {
        Pair<Direction, Direction> inputSides = getInputSides(state);
        return world.getEmittedRedstonePower(pos.offset(inputSides.getRight()), inputSides.getRight());
    }

}