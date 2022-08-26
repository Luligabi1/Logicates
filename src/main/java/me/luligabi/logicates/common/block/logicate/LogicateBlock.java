package me.luligabi.logicates.common.block.logicate;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractRedstoneGateBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class LogicateBlock extends AbstractRedstoneGateBlock {

    protected LogicateBlock() {
        super(FabricBlockSettings.copyOf(Blocks.REPEATER));
        setDefaultState(getStateManager().getDefaultState().with(FACING, Direction.NORTH).with(POWERED, false));
    }

    protected abstract LogicateType getLogicateType();

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED);
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

    protected abstract List<MutableText> getLogicateTooltip();

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        tooltip.add(getLogicateType().getText().formatted(Formatting.BLUE));
        tooltip.add(new LiteralText(""));
        for(MutableText text : getLogicateTooltip()) {
            tooltip.add(text.formatted(Formatting.GRAY));
        }
    }
}