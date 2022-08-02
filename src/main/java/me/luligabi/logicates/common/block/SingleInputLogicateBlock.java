package me.luligabi.logicates.common.block;

import me.luligabi.logicates.common.block.property.InputSides;
import me.luligabi.logicates.common.block.property.PropertyRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public abstract class SingleInputLogicateBlock extends LogicateBlock {

    protected SingleInputLogicateBlock() {
        super();
        setDefaultState(getStateManager().getDefaultState().with(FACING, Direction.NORTH).with(POWERED, false).with(SINGLE_INPUT_SIDE, InputSides.BACK));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(!player.getAbilities().allowModifyWorld) return ActionResult.PASS;
        world.setBlockState(pos, state.cycle(SINGLE_INPUT_SIDE));
        updateNeighbors(state, world, pos);
        return ActionResult.success(world.isClient);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if(!moved && !state.isOf(newState.getBlock())) {
            if(state.get(POWERED)) {
                updateNeighbors(state, world, pos);
            }

            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    public Direction getInputSide(BlockState state) {
        Direction direction = state.get(FACING);
        switch(state.get(SINGLE_INPUT_SIDE)) {
            case LEFT -> {
                return getInputSideInternal(state);
            }
            case RIGHT -> {
                return getInputSideInternal(state).getOpposite();
            }
            case BACK -> {
                return direction;
            }
            case LEFT_RIGHT, LEFT_BACK, RIGHT_BACK -> throw new IllegalStateException("Illegal dual input blockstate on single input Logicate!");
        }
        return direction;
    }

    private Direction getInputSideInternal(BlockState state) {
        Direction direction = state.get(FACING);
        switch(direction) {
            case NORTH -> {
                return Direction.EAST;
            }
            case SOUTH -> {
                return Direction.WEST;
            }
            case WEST -> {
                return Direction.NORTH;
            }
            case EAST -> {
                return Direction.SOUTH;
            }
        }
        return direction;
    }


    protected void updateNeighbors(BlockState state, World world, BlockPos pos) {
        world.updateNeighborsAlways(pos, this);
        world.updateNeighborsAlways(pos.offset(state.get(FACING).getOpposite()), this);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(SINGLE_INPUT_SIDE);
    }

    protected static final EnumProperty<InputSides> SINGLE_INPUT_SIDE = PropertyRegistry.SINGLE_INPUT_SIDE;
}