package me.luligabi.logicates.common.block;

import me.luligabi.logicates.common.block.property.InputSides;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractRedstoneGateBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public abstract class LogicateBlock extends AbstractRedstoneGateBlock {

    protected LogicateBlock() {
        super(FabricBlockSettings.copyOf(Blocks.REPEATER));
        setDefaultState(getStateManager().getDefaultState().with(FACING, Direction.NORTH).with(POWERED, false));
    }

    abstract EnumProperty<InputSides> getInputSideProperty();

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(!player.getAbilities().allowModifyWorld) return ActionResult.PASS;
        world.setBlockState(pos, state.cycle(getInputSideProperty()));
        updateNeighbors(state, world, pos);
        return ActionResult.success(world.isClient);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED, getInputSideProperty());
    }

    @Override
    protected int getUpdateDelayInternal(BlockState state) {
        return 2;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if(!moved && !state.isOf(newState.getBlock())) {
            if(state.get(POWERED)) {
                updateNeighbors(state, world, pos);
            }

            super.onStateReplaced(state, world, pos, newState, false);
        }
    }

    protected void updateNeighbors(BlockState state, World world, BlockPos pos) {
        world.updateNeighborsAlways(pos, this);
        world.updateNeighborsAlways(pos.offset(state.get(FACING).getOpposite()), this);
    }

    protected Direction getInputSideDirection(BlockState state, boolean leftSide) {
        Direction direction = state.get(FACING);
        switch(direction) {
            case NORTH -> {
                return leftSide ? Direction.EAST : Direction.WEST;
            }
            case SOUTH -> {
                return leftSide ? Direction.WEST : Direction.EAST;
            }
            case WEST -> {
                return leftSide ? Direction.NORTH : Direction.SOUTH;
            }
            case EAST -> {
                return leftSide ? Direction.SOUTH : Direction.NORTH;
            }
        }
        return direction;
    }

}