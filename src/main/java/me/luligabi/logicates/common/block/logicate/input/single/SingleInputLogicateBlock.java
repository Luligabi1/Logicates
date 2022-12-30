package me.luligabi.logicates.common.block.logicate.input.single;

import me.luligabi.logicates.common.block.logicate.LogicateType;
import me.luligabi.logicates.common.block.logicate.input.InputtableLogicateBlock;
import me.luligabi.logicates.common.block.logicate.inputless.weather.property.InputSides;
import me.luligabi.logicates.common.block.logicate.inputless.weather.property.PropertyRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public abstract class SingleInputLogicateBlock extends InputtableLogicateBlock {

    protected SingleInputLogicateBlock() {
        super();
        setDefaultState(getStateManager().getDefaultState().with(FACING, Direction.NORTH).with(POWERED, false).with(getInputSideProperty(), InputSides.BACK));
    }

    @Override
    public LogicateType getLogicateType() {
        return LogicateType.SINGLE_INPUT;
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

    @Override
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