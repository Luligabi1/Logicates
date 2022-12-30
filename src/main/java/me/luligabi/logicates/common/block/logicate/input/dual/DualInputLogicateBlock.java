package me.luligabi.logicates.common.block.logicate.input.dual;

import me.luligabi.logicates.common.block.logicate.LogicateType;
import me.luligabi.logicates.common.block.logicate.input.InputtableLogicateBlock;
import me.luligabi.logicates.common.block.logicate.inputless.weather.property.InputSides;
import me.luligabi.logicates.common.block.logicate.inputless.weather.property.PropertyRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public abstract class DualInputLogicateBlock extends InputtableLogicateBlock {

    protected DualInputLogicateBlock() {
        super();
        setDefaultState(getStateManager().getDefaultState().with(FACING, Direction.NORTH).with(POWERED, false).with(getInputSideProperty(), InputSides.LEFT_RIGHT));
    }

    @Override
    public LogicateType getLogicateType() {
        return LogicateType.DUAL_INPUT;
    }

    @Override
    protected EnumProperty<InputSides> getInputSideProperty() {
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

    @Override // TODO: Improve this when I become clinically insane
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if(!state.get(POWERED)) return;
        Direction direction = state.get(FACING);
        double x = (double) pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.2;
        double y = (double) pos.getY() + 0.4 + (random.nextDouble() - 0.5) * 0.2;
        double z = (double) pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.2;
        float g = -5.0f;
        double xOffset = (g /= 16.0f) * (float) direction.getOffsetX();
        double zOffset = g * (float) direction.getOffsetZ();

        world.addParticle(DustParticleEffect.DEFAULT, x + xOffset, y, z + zOffset, 0.0, 0.0, 0.0);
    }

}